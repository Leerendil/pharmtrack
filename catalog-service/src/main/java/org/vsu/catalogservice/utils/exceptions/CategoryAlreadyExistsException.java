package org.vsu.catalogservice.utils.exceptions;

import lombok.Getter;

@Getter
public class CategoryAlreadyExistsException extends RuntimeException {
    private String name;

    public CategoryAlreadyExistsException(String name) {
        super("Category already exists with name: "+name);
        this.name=name;
    }
}
