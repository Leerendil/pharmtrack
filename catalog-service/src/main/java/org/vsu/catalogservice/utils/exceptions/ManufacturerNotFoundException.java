package org.vsu.catalogservice.utils.exceptions;

import lombok.Getter;

@Getter
public class ManufacturerNotFoundException extends RuntimeException {
    private String name;

    public ManufacturerNotFoundException(String name) {
        super("Manufacturer was not found by name: "+name);
        this.name=name;
    }
}
