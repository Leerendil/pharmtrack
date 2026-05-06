package org.vsu.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.orderservice.dto.OrderResponse;
import org.vsu.orderservice.dto.OrderStatusUpdate;
import org.vsu.orderservice.entity.CartItem;
import org.vsu.orderservice.entity.Order;
import org.vsu.orderservice.mapper.OrderMapper;
import org.vsu.orderservice.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.vsu.orderservice.entity.enums.OrderStatus.CREATED;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    public OrderResponse create(Jwt jwt) {
        String buyerId = jwt.getSubject();

        List<CartItem> cartItems = cartService.getAllCart(jwt);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<Long> medicinesIds = new ArrayList<>();
        for (CartItem item : cartItems) {
            BigDecimal itemPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemPrice);
            medicinesIds.add(item.getMedicineId());
        }

        Order entity = Order.builder()
                .buyerId(UUID.fromString(buyerId))
                .medicinesIds(medicinesIds)
                .totalPrice(totalPrice)
                .status(CREATED)
                .build();

        entity = orderRepository.save(entity);

        cartService.clearCart(jwt);

        return orderMapper.mapToResponse(entity);
    }

    public OrderResponse changeOrderStatus(OrderStatusUpdate updateDto) {
        Order entity = orderRepository.findById(updateDto.getOrderId())
                //TODO: Заменить на свой OrderNotFoundException()
                .orElseThrow(() -> new RuntimeException("404"));

        entity.setStatus(updateDto.getStatus());

        return orderMapper.mapToResponse(orderRepository.save(entity));
    }
}
