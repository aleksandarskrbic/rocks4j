package com.github.aleksandarskrbic.rocksdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.aleksandarskrbic.rocksdb.exception.SerDeException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface KeyValueRepository<K, V> {

    void save(K key, V value) throws SerializationException, RocksDBException;

    Optional<V> findByKey(K key) throws SerDeException, RocksDBException;

    Collection<V> findAll() throws IOException;

    void deleteByKey(K key) throws JsonProcessingException, RocksDBException;

    void deleteAll();
}
