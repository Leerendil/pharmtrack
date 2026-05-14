package org.vsu.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.orderservice.entity.Order;
import org.vsu.orderservice.entity.Outbox;
import org.vsu.orderservice.events.OrderEvent;
import org.vsu.orderservice.repository.OutboxRepository;
import org.vsu.orderservice.utils.exceptions.OutboxMalfunctionException;

@Service
@Transactional
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(OrderEvent event) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(event);

            Outbox entity = Outbox.builder()
                    .orderId(event.getOrderId())
                    .payload(jsonPayload)
                    .build();

            outboxRepository.save(entity);
        } catch (Exception e) {
            throw new OutboxMalfunctionException(e);
        }
    }
}
