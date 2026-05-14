package org.vsu.authservice.utils.exceptions;

import jakarta.ws.rs.core.Response;

public class KeycloakRegisterException extends RuntimeException {
    public KeycloakRegisterException(Response response) {
        super("Failed to register new user in keycloak. Response code: " + response.getStatus());
    }
}
