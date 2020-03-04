package com.github.aleksandarskrbic.rocksdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface KeyValueRepository<K, V> {

    void save(K key, V value) throws JsonProcessingException, RocksDBException;

    Optional<V> findByKey(K key) throws IOException, RocksDBException;

    Collection<V> findAll() throws IOException;

    void deleteByKey(K key) throws JsonProcessingException, RocksDBException;

    void deleteAll() throws JsonProcessingException, RocksDBException;
}
