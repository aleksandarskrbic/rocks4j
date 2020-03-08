package com.github.aleksandarskrbic.rocks4j;

import java.util.Collection;
import java.util.Optional;
import com.github.aleksandarskrbic.rocks4j.exception.DeserializationException;
import com.github.aleksandarskrbic.rocks4j.exception.SerDeException;
import com.github.aleksandarskrbic.rocks4j.exception.SerializationException;
import org.rocksdb.RocksDBException;

/**
 *  Interface that defines operations against Key-Value Store.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public interface KeyValueRepository<K, V> {

    /**
     * Inserts key-value pair into RocksDB.
     *
     * @param key of value.
     * @param value that should be persisted.
     * @throws SerializationException when it's not possible to serialize entity.
     * @throws RocksDBException when it's not possible to persist entity.
     */
    void save(K key, V value) throws SerializationException, RocksDBException;

    /**
     * Try to find value for a given key.
     *
     * @param key of entity that should be retrieved.
     * @return Optional of entity.
     * @throws SerDeException when it's not possible to serialize/deserialize entity.
     * @throws RocksDBException when it's not possible to retrieve a wanted entity.
     */
    Optional<V> findByKey(K key) throws SerDeException, RocksDBException;

    /**
     * Try to find all entities from repository.
     *
     * @return Collection of entities.
     * @throws DeserializationException when it's not possible to deserialize entity.
     */
    Collection<V> findAll() throws DeserializationException;

    /**
     * Delete entity for a given key.
     *
     * @param key of entity that should be deleted.
     * @throws SerializationException when it's not possible to serialize entity.
     * @throws RocksDBException when it's not possible to delete a wanted entity.
     */
    void deleteByKey(K key) throws SerializationException, RocksDBException;

    /**
     * Deletes all entities from RocksDB.
     *
     * @throws RocksDBException when it's not possible to delete entity.
     */
    void deleteAll() throws RocksDBException;
}
