package org.vsu.notificationservice.utils.exception;

public class KafkaMalfunctionException extends RuntimeException {
    public KafkaMalfunctionException(Exception e) {
        super("Failed to parse Kafka message. LocalizedMessage: "+e.getLocalizedMessage());
    }
}
