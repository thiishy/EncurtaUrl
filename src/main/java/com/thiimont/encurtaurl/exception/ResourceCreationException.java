package com.thiimont.encurtaurl.exception;

public class ResourceCreationException extends RuntimeException {
    public ResourceCreationException() { super("Não foi possível criar o recurso."); }
    public ResourceCreationException(String message) { super(message); }
}
