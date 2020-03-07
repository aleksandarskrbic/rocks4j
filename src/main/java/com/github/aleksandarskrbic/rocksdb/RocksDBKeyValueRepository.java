package com.github.aleksandarskrbic.rocksdb;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.aleksandarskrbic.rocksdb.configuration.RocksDBConfiguration;
import com.github.aleksandarskrbic.rocksdb.exception.DeserializationException;
import com.github.aleksandarskrbic.rocksdb.exception.SerializationException;
import com.github.aleksandarskrbic.rocksdb.mapper.RocksDBMapper;
import com.github.aleksandarskrbic.rocksdb.mapper.RocksDBMapperFactory;

/**
 * Base class for concrete repository.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public abstract class RocksDBKeyValueRepository<K, V> extends RocksDBConnection implements KeyValueRepository<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocksDBKeyValueRepository.class);

    private final RocksDBMapper<K> keyMapper;
    private final RocksDBMapper<V> valueMapper;

    public RocksDBKeyValueRepository(final RocksDBConfiguration configuration, final Class<K> keyType, final Class<V> valueType) {
        super(configuration);
        keyMapper = RocksDBMapperFactory.mapperFor(keyType);
        valueMapper = RocksDBMapperFactory.mapperFor(valueType);
    }

    @Override
    public synchronized void save(final K key, final V value) {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            final byte[] serializedValue = valueMapper.serialize(value);
            rocksDB.put(serializedKey, serializedValue);
        } catch (final SerializationException exception) {
            LOGGER.error("Serialization exception occurred during save operation. {}", exception.toString());
        } catch (final RocksDBException exception) {
            LOGGER.error("RocksDBException occurred during save operation. {}", exception.toString());
        }
    }

    @Override
    public Optional<V> findByKey(final K key) {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            final byte[] bytes = rocksDB.get(serializedKey);
            return Optional.ofNullable(valueMapper.deserialize(bytes));
        } catch (final SerializationException exception) {
            LOGGER.error("Serialization exception occurred during findByKey operation. {}", exception.toString());
        } catch (final RocksDBException exception) {
            LOGGER.error("RocksDBException occurred during findByKey operation. {}", exception.toString());
        } catch (final DeserializationException exception) {
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
            } catch (final DeserializationException exception) {
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
        } catch (final SerializationException exception) {
            LOGGER.error("Serialization exception occurred during findByKey operation. {}", exception.toString());
        } catch (final RocksDBException exception) {
            LOGGER.error("RocksDBException occurred during deleteByKey operation. {}", exception.toString());
        }
    }

    @Override
    public void deleteAll() {
        final RocksIterator iterator = rocksDB.newIterator();

        iterator.seekToFirst();
        final byte[] firstKey = getKey(iterator);

        iterator.seekToLast();
        final byte[] lastKey = getKey(iterator);

        if (firstKey == null || lastKey == null) {
            return;
        }

        try {
            rocksDB.deleteRange(firstKey, lastKey);
            rocksDB.delete(lastKey);
        } catch (final RocksDBException exception) {
            LOGGER.error("RocksDBException occurred during deleteAll operation. {}", exception.toString());
        }
    }

    private byte[] getKey(final RocksIterator iterator) {
        if (!iterator.isValid()) {
            return null;
        }
        return iterator.key();
    }
}
