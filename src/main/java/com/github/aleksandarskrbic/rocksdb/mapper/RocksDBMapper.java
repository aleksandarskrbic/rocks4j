package com.github.aleksandarskrbic.rocksdb.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksandarskrbic.rocksdb.exception.DeserializationException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;
import java.io.IOException;

public final class RocksDBMapper<T> implements Mapper<T> {

    private final Class<T> type;
    private final ObjectMapper mapper;

    public RocksDBMapper(final Class<T> type) {
        this.type = type;
        this.mapper = new ObjectMapper();
    }

    public byte[] serialize(final T t) throws SerializationException {
        try {
            return mapper.writeValueAsBytes(t);
        } catch (final JsonProcessingException exception) {
            throw new SerializationException(exception.getMessage());
        }
    }

    public T deserialize(final byte[] bytes) throws DeserializationException {
        try {
            return mapper.readValue(bytes, type);
        } catch (final IOException exception) {
            throw new DeserializationException(exception.getMessage());
        }
    }
}
