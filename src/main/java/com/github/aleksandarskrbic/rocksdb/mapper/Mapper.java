package com.github.aleksandarskrbic.rocksdb.mapper;

import com.github.aleksandarskrbic.rocksdb.exception.DeserializationException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;

/**
 * Interface that defines methods for mapping to bytes.
 *
 * @param <T> Value type that should be serialized or deserialized.
 */
public interface Mapper<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws DeserializationException;
}
