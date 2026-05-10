package org.vsu.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.vsu.notificationservice.dto.OrderEvent;
import org.vsu.notificationservice.service.NotificationService;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${order-topic:order-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void collectMessage(String message) {
        try {
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);



        } catch (Exception e) {
        }
    }
}
