package com.github.aleksandarskrbic.rocks4j.store;

import com.github.aleksandarskrbic.rocks4j.core.ReactiveKeyValueStore;
import com.github.aleksandarskrbic.rocks4j.core.RocksDBConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveKVStore<K, V> implements ReactiveKeyValueStore<K, V> {

	private final AsyncKVStore<K, V> underlying;

	public ReactiveKVStore(final RocksDBConfiguration configuration) {
		this.underlying = new AsyncKVStore<>(configuration);
	}

	@Override
	public Mono<Void> save(final K key, final V value) {
		return Mono.fromCompletionStage(underlying.save(key, value));
	}

	@Override
	public Mono<V> findByKey(final K key) {
		return Mono
			.fromCompletionStage(underlying.findByKey(key))
			.flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty));
	}

	@Override
	public Flux<V> findAll() {
		return Flux.fromIterable(underlying.findAll().join());
	}

	@Override
	public Mono<Void> deleteByKey(final K key) {
		return Mono.fromCompletionStage(underlying.deleteByKey(key));
	}

	@Override
	public Mono<Void> deleteAll() {
		return Mono.fromCompletionStage(underlying.deleteAll());
	}
}
