package org.vsu.orderservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vsu.orderservice.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private UUID orderId;

    private UUID buyerId;

    private String buyerEmail;

    Map<Long, Integer> medicinesIds;

    private OrderStatus status;

    private BigDecimal totalPrice;
}
