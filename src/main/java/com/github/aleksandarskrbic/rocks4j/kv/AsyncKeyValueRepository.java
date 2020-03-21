package com.github.aleksandarskrbic.rocks4j.kv;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import com.github.aleksandarskrbic.rocks4j.exception.DeserializationException;
import com.github.aleksandarskrbic.rocks4j.exception.SerDeException;
import com.github.aleksandarskrbic.rocks4j.exception.SerializationException;
import org.rocksdb.RocksDBException;

/**
 *  Interface that defines asynchronous operations against Key-Value Store.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public interface AsyncKeyValueRepository<K, V> {

    /**
     * Inserts key-value pair into RocksDB asynchronously.
     *
     * @param key of value.
     * @param value that should be persisted.
     * @return CompletableFuture of Void.
     * @throws SerializationException when it's not possible to serialize entity.
     * @throws RocksDBException when it's not possible to persist entity.
     */
    CompletableFuture<Void> save(K key, V value) throws SerializationException, RocksDBException;

    /**
     * Try to find value for a given key asynchronously.
     *
     * @param key of entity that should be retrieved.
     * @return CompletableFuture of Optional of entity.
     * @throws SerDeException when it's not possible to serialize/deserialize entity.
     * @throws RocksDBException when it's not possible to retrieve a wanted entity.
     */
    CompletableFuture<Optional<V>> findByKey(K key) throws SerDeException, RocksDBException;

    /**
     * Try to find all entities from repository asynchronously.
     *
     * @return CompletableFuture of Collection of entities.
     * @throws DeserializationException when it's not possible to deserialize entity.
     */
    CompletableFuture<Collection<V>> findAll() throws DeserializationException;

    /**
     * Delete entity for a given key asynchronously..
     *
     * @param key of entity that should be deleted.
     * @return CompletableFuture of Void.
     * @throws SerializationException when it's not possible to serialize entity.
     * @throws RocksDBException when it's not possible to delete a wanted entity.
     */
    CompletableFuture<Void> deleteByKey(K key) throws SerializationException, RocksDBException;

    /**
     * Deletes all entities from RocksDB asynchronously.
     *
     * @return CompletableFuture of Void.
     * @throws RocksDBException when it's not possible to delete entity.
     */
    CompletableFuture<Void> deleteAll() throws RocksDBException;
}
