package com.github.aleksandarskrbic.rocksdb.mapper;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.aleksandarskrbic.rocksdb.exception.DeserializationException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public final class RocksDBMapper<T> implements Mapper<T> {

    private final Class<T> type;
    private final Kryo kryo;

    public RocksDBMapper(final Class<T> type) {
        this.type = type;
        this.kryo = new Kryo();
        kryo.register(type);
    }

    public byte[] serialize(final T t) throws SerializationException {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final Output output = new Output(byteArrayOutputStream);
            kryo.writeObject(output, t);
            output.flush();
            output.close();
            return byteArrayOutputStream.toByteArray();
        } catch (final Exception exception) {
            throw new SerializationException(exception.getMessage());
        }
    }

    public T deserialize(final byte[] bytes) throws DeserializationException {
        try {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            final Input input = new Input(byteArrayInputStream);
            return kryo.readObject(input, type);
        } catch (final Exception exception) {
            throw new DeserializationException(exception.getMessage());
        }
    }
}
