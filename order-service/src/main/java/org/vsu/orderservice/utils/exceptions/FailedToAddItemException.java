package org.vsu.orderservice.utils.exceptions;

import lombok.Getter;

public class FailedToAddItemException extends RuntimeException {
    public FailedToAddItemException(Exception ex) {
        super("Failed to add item. LocalizedMessage: "+ex.getLocalizedMessage());
    }
}
