package com.github.aleksandarskrbic.rocks4j.core;

import com.github.aleksandarskrbic.rocks4j.core.exception.DeleteAllFailedException;
import com.github.aleksandarskrbic.rocks4j.core.exception.DeleteFailedException;
import com.github.aleksandarskrbic.rocks4j.core.exception.FindFailedException;
import com.github.aleksandarskrbic.rocks4j.core.exception.SaveFailedException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.DeserializationException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerDeException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerializationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;

public interface ReactiveKeyValueStore<K, V> {

	/**
	 * Inserts key-value pair into RocksDB.
	 *
	 * @param key of value.
	 * @param value that should be persisted.
	 * @throws SerializationException when it's not possible to serialize entity.
	 * @throws SaveFailedException when it's not possible to persist entity.
	 */
	Mono<Void> save(K key, V value);

	/**
	 * Try to find value for a given key.
	 *
	 * @param key of entity that should be retrieved.
	 * @return Optional of entity.
	 * @throws SerDeException when it's not possible to serialize/deserialize entity.
	 * @throws FindFailedException when it's not possible to retrieve a wanted entity.
	 */
	Mono<V> findByKey(K key);

	/**
	 * Try to find all entities from repository.
	 *
	 * @return Collection of entities.
	 * @throws DeserializationException when it's not possible to deserialize entity.
	 */
	Flux<V> findAll();

	/**
	 * Delete entity for a given key.
	 *
	 * @param key of entity that should be deleted.
	 * @throws SerializationException when it's not possible to serialize entity.
	 * @throws DeleteFailedException when it's not possible to delete a wanted entity.
	 */
	Mono<Void>  deleteByKey(K key);

	/**
	 * Deletes all entities from RocksDB.
	 *
	 * @throws DeleteAllFailedException when it's not possible to delete all entities.
	 */
	Mono<Void> deleteAll();
}
