package org.vsu.catalogservice.utils.exceptions;

public class ApplicationMalfunctionException extends RuntimeException {
    public ApplicationMalfunctionException(Exception e) {
        super("Failed to create application. LocalizedMessage: "+e.getLocalizedMessage());
    }
}
