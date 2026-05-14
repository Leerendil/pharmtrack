package org.vsu.userservice.utils.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private String email;

    public UserNotFoundException(String email) {
        super("User was not found by email: "+email);
        this.email=email;
    }
}
