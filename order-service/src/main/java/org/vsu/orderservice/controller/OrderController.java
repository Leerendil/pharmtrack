package org.vsu.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.orderservice.dto.OrderResponse;
import org.vsu.orderservice.dto.OrderStatusUpdate;
import org.vsu.orderservice.service.OrderService;

import java.util.UUID;

import static org.vsu.orderservice.utils.constants.CommonConstants.API_V1_ORDERS;

@RestController
@RequestMapping(API_V1_ORDERS)
@RequiredArgsConstructor
public class OrderController implements OrderAPI {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(jwt));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin")
    public ResponseEntity<OrderResponse> changeOrderStatus(OrderStatusUpdate updateDto) {
        return ResponseEntity.ok(orderService.superviseOrderStatus(updateDto));
    }

    @PatchMapping
    public ResponseEntity<OrderResponse> manageOrderStatus(@RequestBody OrderStatusUpdate updateDto) {
        return ResponseEntity.ok(orderService.manageOrderStatus(updateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getById(id));
    }
}
