package br.com.vscgabriel.baozistore.service.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resource, String field, Object value) {
        super(resource + " already exists for " + field + " " + value);
    }
}
