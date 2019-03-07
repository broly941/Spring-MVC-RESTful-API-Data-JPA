package com.intexsoft.devi.exception;

/**
 * Custom Exception.
 * It will execute when come in exception from database.
 */
public class ViolationException extends RuntimeException {
    public ViolationException() {
        super();
    }

    public ViolationException(String s) {
        super(s);
    }

    public ViolationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ViolationException(Throwable throwable) {
        super(throwable);
    }
}
