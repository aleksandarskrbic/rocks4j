package com.github.aleksandarskrbic.rocks4j.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.DeserializationException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerializationException;

/**
 * RocksDBMapper provides methods for serialization and deserialization using Kryo.
 *
 * @param <T> Value type that should be serialized or deserialized.
 */
public final class RocksDBMapper<T> implements Mapper<T> {

    private final Class<T> type;
    private final Kryo kryo = new Kryo();

    public RocksDBMapper(final Class<T> type) {
        this.type = type;
        kryo.register(type);
    }

    public byte[] serialize(final T t) throws SerializationException {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final Output output = new Output(outputStream);
            kryo.writeObject(output, t);
            output.flush();
            output.close();
            return outputStream.toByteArray();
        } catch (final IllegalArgumentException | KryoException exception) {
            throw new SerializationException(exception.getMessage(), exception);
        }
    }

    public T deserialize(final byte[] bytes) throws DeserializationException {
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            final Input input = new Input(inputStream);
            return kryo.readObject(input, type);
        } catch (final IllegalArgumentException | KryoException exception) {
            throw new DeserializationException(exception.getMessage(), exception);
        }
    }
}
