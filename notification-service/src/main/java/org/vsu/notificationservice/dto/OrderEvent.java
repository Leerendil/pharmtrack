package org.vsu.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vsu.notificationservice.enums.OrderStatus;

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

    Map<Long, Integer> medicinesIds;

    private OrderStatus status;

    private BigDecimal totalPrice;
}
