package com.github.aleksandarskrbic.rocksdb;

/**
 * Configuration class for RocksDB.
 * Path is path to files where data will be stored.
 * Name is the name of repository.
 */
public final class RocksDBConfiguration {

    protected String path;
    protected String name;

    public RocksDBConfiguration() {
    }

    public RocksDBConfiguration(final String path, final String name) {
        this.path = path;
        this.name = name;
    }

    public String path() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String name() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
