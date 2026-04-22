package org.vsu.userservice.utils.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private String email;

    public UserAlreadyExistsException(String email) {
        super("User with such email already exists! Email: "+email);
        this.email=email;
    }
}
