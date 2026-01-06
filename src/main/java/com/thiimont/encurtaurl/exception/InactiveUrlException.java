package com.thiimont.encurtaurl.exception;

public class InactiveUrlException extends RuntimeException {
    public InactiveUrlException() { super("A URL está inativa."); }
    public InactiveUrlException(String message) { super(message); }
}
