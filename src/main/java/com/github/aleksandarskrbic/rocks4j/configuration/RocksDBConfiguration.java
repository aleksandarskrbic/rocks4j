package com.github.aleksandarskrbic.rocks4j.configuration;

/**
 * Configuration class for RocksDB.
 * Path is path to files where data will be stored.
 * Name is the name of repository.
 */
public class RocksDBConfiguration {

    protected String path;
    protected String name;
    protected int threadCount = 5;

    public RocksDBConfiguration() {
    }

    public RocksDBConfiguration(
            final String path,
            final String name
    ) {
        this.path = path;
        this.name = name;
    }

    public RocksDBConfiguration(
            final String path,
            final String name,
            final int threadCount
    ) {
        this.path = path;
        this.name = name;
        this.threadCount = threadCount;
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

    public int threadCount() {
        return threadCount;
    }

    public void setThreadCount(final int threadCount) {
        this.threadCount = threadCount;
    }

    public String url() {
        return path + "/" + name;
    }
}
