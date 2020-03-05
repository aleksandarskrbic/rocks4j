package com.github.aleksandarskrbic.rocksdb;

import com.github.aleksandarskrbic.rocksdb.exception.DeserializationException;
import com.github.aleksandarskrbic.rocksdb.exception.SerDeException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;
import org.rocksdb.RocksDBException;

import java.util.Collection;
import java.util.Optional;

public interface KeyValueRepository<K, V> {

    void save(K key, V value) throws SerializationException, RocksDBException;

    Optional<V> findByKey(K key) throws SerDeException, RocksDBException;

    Collection<V> findAll() throws DeserializationException;

    void deleteByKey(K key) throws SerializationException, RocksDBException;

    void deleteAll() throws RocksDBException;
}
