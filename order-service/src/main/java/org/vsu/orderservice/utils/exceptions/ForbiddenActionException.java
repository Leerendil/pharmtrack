package org.vsu.orderservice.utils.exceptions;

import lombok.Getter;

@Getter
public class ForbiddenActionException extends RuntimeException {
    String action;

    public ForbiddenActionException(String action) {
        super("This action is forbidden. Action: "+action);
        this.action=action;
    }
}
