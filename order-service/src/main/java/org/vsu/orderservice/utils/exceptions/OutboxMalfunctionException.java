package org.vsu.orderservice.utils.exceptions;

public class OutboxMalfunctionException extends RuntimeException {
    public OutboxMalfunctionException(Exception e) {
        super("Outbox malfunction exception. LocalizedMessage: " + e.getLocalizedMessage());
    }
}
