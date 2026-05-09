package org.vsu.orderservice.utils.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderNotFoundException extends RuntimeException {
    private UUID orderId;

    public OrderNotFoundException(UUID orderId) {
        super("Order was not found with id: "+orderId);
        this.orderId=orderId;
    }
}
