package com.github.aleksandarskrbic.rocks4j.mapper;

public interface MapperFactory {

    static <T> JsonMapper<T> createFor(final Class<T> type) {
        return new JsonMapper<>(type);
    }
}
