package com.github.aleksandarskrbic.rocksdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class Mapper<T> {

    private final Class<T> type;
    private final ObjectMapper mapper;

    public Mapper(final Class<T> type) {
        this.type = type;
        this.mapper = new ObjectMapper();
    }

    public byte[] serialize(final T t) throws JsonProcessingException {
        return mapper.writeValueAsBytes(t);
    }

    public T deserialize(final byte[] bytes) throws IOException {
        return mapper.readValue(bytes, type);
    }
}
