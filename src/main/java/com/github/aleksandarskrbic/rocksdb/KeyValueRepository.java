package com.github.aleksandarskrbic.rocksdb;

import java.util.Collection;
import java.util.Optional;
import org.rocksdb.RocksDBException;
import com.github.aleksandarskrbic.rocksdb.exception.DeserializationException;
import com.github.aleksandarskrbic.rocksdb.exception.SerDeException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;

/**
 *  Interface that defines operations against Key-Value Store
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public interface KeyValueRepository<K, V> {

    void save(K key, V value) throws SerializationException, RocksDBException;

    Optional<V> findByKey(K key) throws SerDeException, RocksDBException;

    Collection<V> findAll() throws DeserializationException;

    void deleteByKey(K key) throws SerializationException, RocksDBException;

    void deleteAll() throws RocksDBException;
}
