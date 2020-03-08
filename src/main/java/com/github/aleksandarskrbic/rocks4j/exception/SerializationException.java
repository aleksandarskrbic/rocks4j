package com.github.aleksandarskrbic.rocks4j.exception;

/**
 * This exception is thrown when there is a problem with serialization.
 */
public final class SerializationException extends SerDeException {

    public SerializationException(final String message) {
        super(message);
    }

    public SerializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
