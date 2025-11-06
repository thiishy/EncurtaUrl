package com.thiimont.encurtaurl.exception;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException() { super("A URL encurtada não existe."); }

    public UrlNotFoundException(String message) { super(message); }
}
