package br.com.vscgabriel.baozistore.service.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " with id " + id + " was not found");
    }

    public ResourceNotFoundException(String resource, String identifier) {
        super(resource + " with identifier " + identifier + " was not found");
    }
}
