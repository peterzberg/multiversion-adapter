package dev.zberg.quarkus.multiversion.runtime.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

class AdapterCache<T> {
    private final long cacheValidityInMillis;

    private CacheEntry<T> cachedInstance;

    AdapterCache(final long cacheValidityInMillis) {
        this.cacheValidityInMillis = cacheValidityInMillis;
    }

    Optional<T> get() {
        return Optional.ofNullable(cachedInstance)
                .filter(this::isNotExpired)
                .map(CacheEntry::instance);
    }

    void set(T instance) {
        this.cachedInstance = new CacheEntry<>(instance);
    }

    private boolean isNotExpired(final CacheEntry<T> entry) {
        return LocalDateTime.now().isBefore(entry.creationDate().plus(cacheValidityInMillis, ChronoUnit.MILLIS));
    }

    private record CacheEntry<T>(T instance, LocalDateTime creationDate) {
        CacheEntry(T instance) {
            this(instance, LocalDateTime.now());
        }
    }
}
