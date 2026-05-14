package org.vsu.catalogservice.utils.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    private String name;

    public CategoryNotFoundException(String name) {
        super("Category was not found by name: "+name);
        this.name=name;
    }
}
