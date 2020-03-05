package com.github.aleksandarskrbic.rocksdb.mapper;

import com.github.aleksandarskrbic.rocksdb.exception.DeserializationException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;

public interface Mapper<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws DeserializationException;
}
