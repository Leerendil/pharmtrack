package org.vsu.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.vsu.notificationservice.dto.OrderEvent;
import org.vsu.notificationservice.service.NotificationService;
import org.vsu.notificationservice.utils.exception.KafkaMalfunctionException;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${order-topic:order-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void collectMessage(String message) {
        try {
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);

            String subject = "Pharmtrack-Notification <Order>.";
            String text = formMessage(event);

            notificationService.sendMessage(event.getBuyerEmail(), subject, text);

        } catch (Exception e) {
            throw new KafkaMalfunctionException(e);
        }
    }

    private String formMessage(OrderEvent event) {
        return switch (event.getStatus()) {
            case CREATED ->
                    "Pharmtrack check.\nOrder Id: " + event.getOrderId() + ".\nTotal coast: " + event.getTotalPrice();
            case PAID ->
                    "Pharmtrack notification-message.\nYour order " + event.getOrderId() + " was paid.";
            case TRANSIT ->
                    "Pharmtrack notification-message.\nYour order " + event.getOrderId() + " was sent to transint.";
            case DELIVERED ->
                    "Pharmtrack notification-message.\nYour order " + event.getOrderId() + " was delivered.";
            case CANCELLED ->
                    "Pharmtrack notification-message.\nYour order " + event.getOrderId() + " was canceled.";
            default -> " ";
        };
    }
}
