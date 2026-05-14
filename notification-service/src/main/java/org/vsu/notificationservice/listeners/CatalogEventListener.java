package org.vsu.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.vsu.notificationservice.dto.ApplicationEvent;
import org.vsu.notificationservice.service.NotificationService;
import org.vsu.notificationservice.utils.exception.KafkaMalfunctionException;

@Component
@RequiredArgsConstructor
public class CatalogEventListener {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Value("${admin-mail}")
    private String adminMail;

    @KafkaListener(topics = "${application-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void collectMessage(String message) {
        try {
            ApplicationEvent event = objectMapper.readValue(message, ApplicationEvent.class);

            String subject = "Pharmtrack admin-notification. New Manufacturer Status Request.";
            String text = """
                    Hello, a new application for MANUFACTURER status has been submitted.\n
                    User Details:
                    \n\tApplication Token:\t""" + event.getApplicationId() + """
                    \n\tUser ID:\t""" + event.getApplicantId() + """
                    \n\tCompany name:\t""" + event.getName() + """
                    \n\tCompany email:\t""" + event.getCompanyMail() + """
                    \n\tCountry:\t""" + event.getCountry() + """
                    \n\tPlease review the application and take action.
                    """;

            notificationService.sendMessage(adminMail, subject, text);

        } catch (Exception e) {
            throw new KafkaMalfunctionException(e);
        }
    }
}
