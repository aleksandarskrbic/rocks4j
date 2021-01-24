package com.github.aleksandarskrbic.rocks4j.core.exception;

/**
 * {@link SaveFailedException} is thrown when there is a problem with saving an entity to a repository.
 */
public final class SaveFailedException extends RocksIOException {

    public SaveFailedException(final String message) {
        super(message);
    }

    public SaveFailedException(
            final String message,
            final Throwable throwable
    ) {
        super(message, throwable);
    }
}
