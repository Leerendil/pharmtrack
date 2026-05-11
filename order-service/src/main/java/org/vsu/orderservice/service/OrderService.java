package org.vsu.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.vsu.orderservice.dto.OrderResponse;
import org.vsu.orderservice.dto.OrderStatusUpdate;
import org.vsu.orderservice.entity.CartItem;
import org.vsu.orderservice.entity.Order;
import org.vsu.orderservice.entity.Outbox;
import org.vsu.orderservice.events.OrderEvent;
import org.vsu.orderservice.mapper.OrderMapper;
import org.vsu.orderservice.repository.OrderRepository;
import org.vsu.orderservice.repository.OutboxRepository;
import org.vsu.orderservice.utils.exceptions.ForbiddenActionException;
import org.vsu.orderservice.utils.exceptions.OrderNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.vsu.orderservice.entity.enums.OrderStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxService outboxService;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    public OrderResponse create(Jwt jwt) {
        String buyerId = jwt.getSubject();
        String buyerMail = jwt.getClaimAsString("email");

        List<CartItem> cartItems = cartService.getAllCart(jwt);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Map<Long, Integer> medicines = new HashMap<>();
        for (CartItem item : cartItems) {
            BigDecimal itemPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemPrice);
            medicines.put(item.getMedicineId(), item.getQuantity());
        }

        Order entity = Order.builder()
                .buyerId(UUID.fromString(buyerId))
                .buyerEmail(buyerMail)
                .medicinesIds(medicines)
                .totalPrice(totalPrice)
                .status(CREATED)
                .build();

        entity = orderRepository.save(entity);
        outboxService.save(
                OrderEvent.builder()
                        .orderId(entity.getId())
                        .buyerId(entity.getBuyerId())
                        .buyerEmail(entity.getBuyerEmail())
                        .medicinesIds(entity.getMedicinesIds())
                        .totalPrice(entity.getTotalPrice())
                        .status(entity.getStatus())
                        .build()
        );

        cartService.clearCart(jwt);

        return orderMapper.mapToResponse(entity);
    }

    public OrderResponse superviseOrderStatus(OrderStatusUpdate updateDto) {
        Order entity = orderRepository.findById(updateDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(updateDto.getOrderId()));

        entity.setStatus(updateDto.getStatus());
        entity = orderRepository.save(entity);

        outboxService.save(
                OrderEvent.builder()
                        .orderId(entity.getId())
                        .buyerId(entity.getBuyerId())
                        .buyerEmail(entity.getBuyerEmail())
                        .medicinesIds(entity.getMedicinesIds())
                        .totalPrice(entity.getTotalPrice())
                        .status(entity.getStatus())
                        .build()
        );

        return orderMapper.mapToResponse(entity);
    }

    public OrderResponse manageOrderStatus(OrderStatusUpdate updateDto) {

        if (updateDto.getStatus() != PAID && updateDto.getStatus() != CANCELLED) {
            throw new ForbiddenActionException(
                    "Status change action. Status: " + updateDto.getStatus()
            );
        }

        Order entity = orderRepository.findById(updateDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(updateDto.getOrderId()));

        entity.setStatus(updateDto.getStatus());
        entity = orderRepository.save(entity);

        return orderMapper.mapToResponse(entity);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void task() {
        List<Order> list = orderRepository.findAllByStatusAndCreatedAtBefore(CREATED, LocalDateTime.now().minusWeeks(1));
        orderRepository.deleteAll(list);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(UUID orderId) {
        return orderMapper.mapToResponse(orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId)));
    }

}
