package org.vsu.authservice.utils.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User with such credentials already exists");
    }
}
