package org.vsu.catalogservice.utils.exceptions;

public class KafkaMalfunctionExecution extends RuntimeException {
    public KafkaMalfunctionExecution(Exception e) {
        super("Failed to send Kafka message. LocalizedMessage: "+ e.getLocalizedMessage());
    }
}
