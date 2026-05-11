package org.vsu.orderservice.utils.exceptions;

public class KafkaMalfunctionException extends RuntimeException {
    public KafkaMalfunctionException(Exception e) {
        super("Kafka malfunction exception. LocalizedMessage: " + e.getLocalizedMessage());
    }
}