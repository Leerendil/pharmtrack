package org.vsu.catalogservice.utils.exceptions;

import lombok.Getter;

@Getter
public class MedicineNotFoundException extends RuntimeException {
    private String name;

    public MedicineNotFoundException(String name) {
        super("Medicine not found by name: "+name);
        this.name=name;
    }
}
