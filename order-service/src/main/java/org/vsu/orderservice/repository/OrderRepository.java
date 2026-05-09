package org.vsu.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vsu.orderservice.entity.Order;
import org.vsu.orderservice.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime createdAtBefore);
}
