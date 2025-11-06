package com.thiimont.encurtaurl.exception;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException() { super("A URL específicada é inválida."); }
    public InvalidUrlException(String message) { super(message); }
}
