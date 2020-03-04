package com.github.aleksandarskrbic.rocksdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

public abstract class RocksDBKeyValueRepository<K, V> extends RocksDBConnector implements KeyValueRepository<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocksDBKeyValueRepository.class);

    private final Mapper<K> keyMapper;
    private final Mapper<V> valueMapper;

    public RocksDBKeyValueRepository(final RocksDBConfiguration configuration, final Class<K> keyType, final Class<V> valueType) {
        super(configuration);
        keyMapper = new Mapper<>(keyType);
        valueMapper = new Mapper<>(valueType);
    }

    @Override
    public synchronized void save(final K key, final V value) {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            final byte[] serializedValue = valueMapper.serialize(value);
            rocksDB.put(serializedKey, serializedValue);
        } catch (final JsonProcessingException exception) {
            LOGGER.error("Serialization exception occurred during save operation. {}", exception.toString());
        } catch (final RocksDBException exception) {
            LOGGER.error("Exception occurred during save operation. {}", exception.toString());
        }
    }

    @Override
    public Optional<V> findByKey(final K key) {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            final byte[] bytes = rocksDB.get(serializedKey);
            return Optional.ofNullable(valueMapper.deserialize(bytes));
        } catch (final JsonProcessingException exception) {
            LOGGER.error("Serialization exception occurred during findByKey operation. {}", exception.toString());
        } catch (final RocksDBException exception) {
            LOGGER.error("Exception occurred during findByKey operation. {}", exception.toString());
        } catch (final IOException exception) {
            LOGGER.error("Deserialization exception occurred during findByKey operation. {}", exception.toString());
        }

        return Optional.empty();
    }

    @Override
    public Collection<V> findAll() {
        final Collection<V> result = new LinkedList<>();
        final RocksIterator iterator = rocksDB.newIterator();
        iterator.seekToFirst();

        while (iterator.isValid()) {
            try {
                final V value = valueMapper.deserialize(iterator.value());
                result.add(value);
                iterator.next();
            } catch (final IOException exception) {
                LOGGER.error("Deserialization exception occurred during findAll operation. {}", exception.toString());
                return Collections.emptyList();
            }
        }

        return result;
    }

    @Override
    public void deleteByKey(final K key) {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            rocksDB.delete(serializedKey);
        } catch (final JsonProcessingException exception) {

        } catch (final RocksDBException exception) {

        }
    }

    @Override
    public void deleteAll() throws JsonProcessingException, RocksDBException {

    }
}
