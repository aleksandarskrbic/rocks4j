package com.github.aleksandarskrbic.rocks4j;

/**
 * Configuration class for RocksDB.
 * Path is path to files where data will be stored.
 * Name is the name of repository.
 */
public final class RocksDBConfiguration {

    protected final String path;
    protected final String name;

    public RocksDBConfiguration(
            final String path,
            final String name
    ) {
        this.path = path;
        this.name = name;
    }

    public String path() {
        return path;
    }

    public String name() {
        return name;
    }

    public String url() {
        return path + "/" + name;
    }
}
