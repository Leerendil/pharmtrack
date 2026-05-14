package org.vsu.catalogservice.utils.exceptions;

import lombok.Getter;

@Getter
public class MedicineAlreadyExistsException extends RuntimeException {
    private String name;

    public MedicineAlreadyExistsException(String name) {
        super(String.format("Medicine %s already exists", name));
        this.name=name;
    }
}
