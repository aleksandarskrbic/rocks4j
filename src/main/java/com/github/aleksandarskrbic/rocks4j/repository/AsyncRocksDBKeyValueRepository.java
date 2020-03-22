package com.github.aleksandarskrbic.rocks4j.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.github.aleksandarskrbic.rocks4j.configuration.RocksDBConfiguration;
import com.github.aleksandarskrbic.rocks4j.configuration.RocksDBConnection;
import com.github.aleksandarskrbic.rocks4j.kv.AsyncKeyValueRepository;
import com.github.aleksandarskrbic.rocks4j.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class that should be extended by the concrete asynchronous repository.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class AsyncRocksDBKeyValueRepository<K, V> extends RocksDBConnection implements AsyncKeyValueRepository<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRocksDBKeyValueRepository.class);

    private final RocksDBKeyValueRepository<K, V> repository;
    private final ExecutorService executorService;

    /**
     * Default constructor which automatically infers key and value types needed for mapper creation.
     * Uses {@link com.github.aleksandarskrbic.rocks4j.mapper.RocksDBMapper}.
     *
     * @param configuration for {@link RocksDBConnection}.
     */
    public AsyncRocksDBKeyValueRepository(final RocksDBConfiguration configuration) {
        super(configuration);
        this.repository = new RocksDBKeyValueRepository<>(configuration);
        this.executorService = Executors.newFixedThreadPool(configuration.threadCount());
    }

    /**
     *
     * @param configuration for {@link RocksDBConnection}.
     * @param keyType for mapper.
     * @param valueType for mapper.
     */
    public AsyncRocksDBKeyValueRepository(
            final RocksDBConfiguration configuration,
            final Class<K> keyType,
            final Class<V> valueType
    ) {
        super(configuration);
        this.repository = new RocksDBKeyValueRepository<>(configuration, keyType, valueType);
        this.executorService = Executors.newFixedThreadPool(configuration.threadCount());
    }

    /**
     *
     * @param configuration for {@link RocksDBConnection}.
     * @param keyMapper custom key mapper that implements {@link Mapper}.
     * @param valueMapper custom value mapper that implements {@link Mapper}.
     */
    public AsyncRocksDBKeyValueRepository(
            final RocksDBConfiguration configuration,
            final Mapper<K> keyMapper,
            final Mapper<V> valueMapper
    ) {
        super(configuration);
        this.repository = new RocksDBKeyValueRepository<>(configuration, keyMapper, valueMapper);
        this.executorService = Executors.newFixedThreadPool(configuration.threadCount());
    }

    @Override
    public CompletableFuture<Void> save(
            final K key,
            final V value
    ) {
        return CompletableFuture.runAsync(() -> repository.save(key, value), executorService);
    }

    @Override
    public CompletableFuture<Optional<V>> findByKey(final K key) {
        return CompletableFuture.supplyAsync(() -> repository.findByKey(key), executorService);
    }

    @Override
    public CompletableFuture<Collection<V>> findAll() {
        return CompletableFuture.supplyAsync(repository::findAll, executorService);
    }

    @Override
    public CompletableFuture<Void> deleteByKey(final K key) {
        return CompletableFuture.runAsync(() -> repository.deleteByKey(key), executorService);
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.runAsync(repository::deleteAll, executorService);
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
