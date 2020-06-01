package com.github.aleksandarskrbic.rocks4j.mapper;

public interface RocksDBMapperFactory {

    static <T> RocksDBMapper<T> mapperFor(final Class<T> type) {
        return new RocksDBMapper<T>(type);
    }
}
