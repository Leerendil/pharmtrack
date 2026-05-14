package org.vsu.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.vsu.notificationservice.utils.exception.MailSenderException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendMessage(String receiverMail, String subject, String text) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(mailFrom);
            mailMessage.setTo(receiverMail);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);

            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new MailSenderException(e);
        }
    }
}
