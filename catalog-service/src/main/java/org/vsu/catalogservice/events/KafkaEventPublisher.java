package org.vsu.catalogservice.events;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.entity.Outbox;
import org.vsu.catalogservice.repository.OutboxRepository;
import org.vsu.catalogservice.utils.exceptions.KafkaMalfunctionExecution;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class KafkaEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;

    @Value("${application-topic}")
    private String APPLICATION_TOPIC;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void sendEvent() {
        try {
            List<Outbox> list = outboxRepository.findAllAndLock();

            for (Outbox event : list) {
                String messageKey = event.getId().toString();

                kafkaTemplate.send(APPLICATION_TOPIC, messageKey, event.getPayload());
            }

            outboxRepository.deleteAll(list);
        } catch (Exception e) {
            throw new KafkaMalfunctionExecution(e);
        }
    }

}
