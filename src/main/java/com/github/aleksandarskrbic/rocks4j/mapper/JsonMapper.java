
package com.github.aleksandarskrbic.rocks4j.mapper;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.DeserializationException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerializationException;

/**
 * RocksDBMapper provides methods for serialization and deserialization using JSON.
 *
 * @param <T> Value type that should be serialized or deserialized.
 */
public final class JsonMapper<T> implements Mapper<T> {

    private final Class<T> type;
    private final ObjectMapper mapper;

    public JsonMapper(final Class<T> type) {
        this.type = type;
        this.mapper = new ObjectMapper();
    }

    @Override
    public byte[] serialize(final T t) throws SerializationException {
        try {
            return mapper.writeValueAsBytes(t);
        } catch (final JsonProcessingException exception) {
            throw new SerializationException(exception.getMessage(), exception);
        }
    }

    @Override
    public T deserialize(final byte[] bytes) throws DeserializationException {
        try {
            return mapper.readValue(bytes, type);
        } catch (final IOException exception) {
            throw new DeserializationException(exception.getMessage(), exception);
        }
    }
}