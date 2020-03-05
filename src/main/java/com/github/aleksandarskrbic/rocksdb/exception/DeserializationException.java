package com.github.aleksandarskrbic.rocksdb.exception;

public final class DeserializationException extends SerDeException {

    public DeserializationException(final String message) {
        super(message);
    }

    public DeserializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
