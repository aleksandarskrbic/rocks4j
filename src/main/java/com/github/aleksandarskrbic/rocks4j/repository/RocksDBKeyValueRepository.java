package com.github.aleksandarskrbic.rocks4j.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import com.github.aleksandarskrbic.rocks4j.configuration.RocksDBConfiguration;
import com.github.aleksandarskrbic.rocks4j.configuration.RocksDBConnection;
import com.github.aleksandarskrbic.rocks4j.kv.KeyValueRepository;
import com.github.aleksandarskrbic.rocks4j.kv.exception.DeleteAllFailedException;
import com.github.aleksandarskrbic.rocks4j.kv.exception.DeleteFailedException;
import com.github.aleksandarskrbic.rocks4j.kv.exception.FindFailedException;
import com.github.aleksandarskrbic.rocks4j.kv.exception.SaveFailedException;
import com.github.aleksandarskrbic.rocks4j.mapper.Mapper;
import com.github.aleksandarskrbic.rocks4j.mapper.RocksDBMapperFactory;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.DeserializationException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerDeException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerializationException;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class that should be extended by the concrete repository.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class RocksDBKeyValueRepository<K, V> extends RocksDBConnection implements KeyValueRepository<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocksDBKeyValueRepository.class);

    private final Mapper<K> keyMapper;
    private final Mapper<V> valueMapper;

    /**
     * Default constructor which automatically infers key and value types needed for mapper creation.
     * Uses {@link com.github.aleksandarskrbic.rocks4j.mapper.RocksDBMapper}.
     *
     * @param configuration for {@link RocksDBConnection}.
     */
    public RocksDBKeyValueRepository(final RocksDBConfiguration configuration) {
        super(configuration);
        this.keyMapper = RocksDBMapperFactory.mapperFor(extractKeyType());
        this.valueMapper = RocksDBMapperFactory.mapperFor(extractValueType());
    }

    /**
     *
     * @param configuration for {@link RocksDBConnection}.
     * @param keyType for mapper.
     * @param valueType for mapper.
     */
    public RocksDBKeyValueRepository(
            final RocksDBConfiguration configuration,
            final Class<K> keyType,
            final Class<V> valueType
    ) {
        super(configuration);
        this.keyMapper = RocksDBMapperFactory.mapperFor(keyType);
        this.valueMapper = RocksDBMapperFactory.mapperFor(valueType);
    }

    /**
     *
     * @param configuration for {@link RocksDBConnection}.
     * @param keyMapper custom key mapper that implements {@link Mapper}.
     * @param valueMapper custom value mapper that implements {@link Mapper}.
     */
    public RocksDBKeyValueRepository(
            final RocksDBConfiguration configuration,
            final Mapper<K> keyMapper,
            final Mapper<V> valueMapper
    ) {
        super(configuration);
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
    }

    @Override
    public void save(
            final K key,
            final V value
    ) throws SerializationException, SaveFailedException {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            final byte[] serializedValue = valueMapper.serialize(value);
            rocksDB.put(serializedKey, serializedValue);
        } catch (final SerializationException exception) {
            LOGGER.error("Serialization exception occurred during save operation. {}", exception.getMessage());
            throw exception;
        } catch (final RocksDBException exception) {
            LOGGER.error("RocksDBException occurred during save operation. {}", exception.getMessage());
            throw new SaveFailedException(exception.getMessage(), exception);
        }
    }

    @Override
    public Optional<V> findByKey(final K key) throws SerDeException, FindFailedException {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            final byte[] bytes = rocksDB.get(serializedKey);
            return Optional.ofNullable(valueMapper.deserialize(bytes));
        } catch (final SerializationException exception) {
            LOGGER.error("Serialization exception occurred during findByKey operation. {}", exception.getMessage());
            throw exception;
        } catch (final RocksDBException exception) {
            LOGGER.error("RocksDBException occurred during findByKey operation. {}", exception.getMessage());
            throw new FindFailedException(exception.getMessage(), exception);
        } catch (final DeserializationException exception) {
            LOGGER.error("Deserialization exception occurred during findByKey operation. {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Collection<V> findAll() throws DeserializationException {
        final Collection<V> result = new LinkedList<>();
        final RocksIterator iterator = rocksDB.newIterator();
        iterator.seekToFirst();

        try {
            while (iterator.isValid()) {
                final V value = valueMapper.deserialize(iterator.value());
                result.add(value);
                iterator.next();
            }
        } catch (final DeserializationException exception) {
            LOGGER.error("Deserialization exception occurred during findAll operation. {}", exception.getMessage());
            throw exception;
        } finally {
            iterator.close();
        }

        return result;
    }

    @Override
    public void deleteByKey(final K key) throws SerializationException, DeleteFailedException {
        try {
            final byte[] serializedKey = keyMapper.serialize(key);
            rocksDB.delete(serializedKey);
        } catch (final SerializationException exception) {
            LOGGER.error("Serialization exception occurred during findByKey operation. {}", exception.getMessage());
            throw exception;
        } catch (final RocksDBException exception) {
            LOGGER.error("RocksDBException occurred during deleteByKey operation. {}", exception.getMessage());
            throw new DeleteFailedException(exception.getMessage(), exception);
        }
    }

    @Override
    public void deleteAll() throws DeleteAllFailedException {
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
            LOGGER.error("RocksDBException occurred during deleteAll operation. {}", exception.getMessage());
            throw new DeleteAllFailedException(exception.getMessage(), exception);
        } finally {
            iterator.close();
        }
    }

    private byte[] getKey(final RocksIterator iterator) {
        if (!iterator.isValid()) {
            return null;
        }
        return iterator.key();
    }

    @SuppressWarnings("unchecked")
    private Class<K> extractKeyType() {
        return (Class<K>) extractClass(((ParameterizedType) getGenericSuperClass()).getActualTypeArguments()[0]);
    }

    @SuppressWarnings("unchecked")
    private Class<V> extractValueType() {
        return (Class<V>) extractClass(((ParameterizedType) getGenericSuperClass()).getActualTypeArguments()[1]);
    }

    private Type getGenericSuperClass() {
        final Type superClass = getClass().getGenericSuperclass();

        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }

        return superClass;
    }

    private Class<?> extractClass(final Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }

        throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
    }
}
