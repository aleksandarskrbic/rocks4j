package com.github.aleksandarskrbic.rocks4j.exception;

/**
 * This exception is thrown when there is a problem with deserialization.
 */
public final class DeserializationException extends SerDeException {

    public DeserializationException(final String message) {
        super(message);
    }

    public DeserializationException(
            final String message,
            final Throwable throwable
    ) {
        super(message, throwable);
    }
}
