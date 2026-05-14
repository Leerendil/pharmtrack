package org.vsu.authservice.utils.exceptions;


public class KeycloakLoginException extends RuntimeException {
    public KeycloakLoginException() {
        super("Failed to login. Check your credentials");
    }
}
