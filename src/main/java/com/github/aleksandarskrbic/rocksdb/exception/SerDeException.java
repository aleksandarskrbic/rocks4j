package com.github.aleksandarskrbic.rocksdb.exception;

import java.io.IOException;

/**
 * Base class for serialization/deserialization exceptions.
 */
public abstract class SerDeException extends IOException {

    public SerDeException(final String message) {
        super(message);
    }

    public SerDeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
