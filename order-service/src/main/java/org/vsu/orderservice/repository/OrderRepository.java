package org.vsu.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vsu.orderservice.entity.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
