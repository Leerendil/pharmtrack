package org.vsu.orderservice.events;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.orderservice.entity.Outbox;
import org.vsu.orderservice.repository.OutboxRepository;
import org.vsu.orderservice.utils.exceptions.KafkaMalfunctionException;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class KafkaEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository repository;

    @Value("${order-topic}")
    private String ORDER_TOPIC;

    @Scheduled(fixedDelay = 5000)
    public void sendMessage() {
        try {
            List<Outbox> list = repository.findAllAndLock();

            for (Outbox el : list) {
                String messageKey = (el.getOrderId().toString());
                String jsonPayload = el.getPayload();

                kafkaTemplate.send(ORDER_TOPIC, messageKey, jsonPayload);
            }

            repository.deleteAll(list);
        } catch (Exception e) {
            throw new KafkaMalfunctionException(e);
        }
    }

}
