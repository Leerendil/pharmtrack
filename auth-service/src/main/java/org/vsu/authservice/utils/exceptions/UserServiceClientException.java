package org.vsu.authservice.utils.exceptions;

public class UserServiceClientException extends RuntimeException {
    public UserServiceClientException() {
        super("Failed to create new user in user-service");
    }
}
