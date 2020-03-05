package com.github.aleksandarskrbic.rocksdb.mapper;

public abstract class RocksDBMapperFactory {

    public static <T> RocksDBMapper<T> mapperFor(final Class<T> type) {
        return new RocksDBMapper<T>(type);
    }
}
