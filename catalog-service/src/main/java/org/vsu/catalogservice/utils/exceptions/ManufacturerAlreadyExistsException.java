package org.vsu.catalogservice.utils.exceptions;

import lombok.Getter;

@Getter
public class ManufacturerAlreadyExistsException extends RuntimeException {
    private String name;

    public ManufacturerAlreadyExistsException(String name) {
        super("Manufacturer already exists with name: "+name);
        this.name=name;
    }
}
