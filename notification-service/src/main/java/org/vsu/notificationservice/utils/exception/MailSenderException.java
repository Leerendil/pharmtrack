package org.vsu.notificationservice.utils.exception;

public class MailSenderException extends RuntimeException {
    public MailSenderException(Exception e) {
        super("Failed to send mail message. LocalizedMessage: "+e.getLocalizedMessage());
    }
}
