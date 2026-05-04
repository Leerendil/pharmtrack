package org.vsu.catalogservice.utils.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ApplicationNotFoundException extends RuntimeException {
    private UUID id;

    public ApplicationNotFoundException(UUID id) {
        super("Application was not found by id: "+id);
        this.id=id;
    }
}
