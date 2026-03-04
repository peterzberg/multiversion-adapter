package dev.zberg.quarkus.multiversion.runtime.impl;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import dev.zberg.quarkus.multiversion.runtime.Adapter;
import dev.zberg.quarkus.multiversion.runtime.Multiversioned;

public class AdapterImpl<T extends Multiversioned> implements Adapter<T> {
    private static final Logger LOGGER = Logger.getLogger(AdapterImpl.class.getName());

    private final AdapterLookup<T> adapterLookup;
    private final Class<T> type;
    private final Annotation[] qualifiers;
    private final AdapterCache<T> cache;

    public AdapterImpl(final Class<T> type, final long cacheValidityInMillis, final Annotation... qualifiers) {
        this(new CDIAdapterLookup<T>(), type, cacheValidityInMillis, qualifiers);
    }

    AdapterImpl(final AdapterLookup<T> adapterLookup, final Class<T> type, final long cacheValidityInMillis, final Annotation... qualifiers) {
        this.adapterLookup = adapterLookup;
        this.type = type;
        this.qualifiers = qualifiers;
        cache = new AdapterCache<>(cacheValidityInMillis);
    }

    @Override
    public T get() {
        final Optional<T> instance = this.cache.get();
        if (instance.isPresent()) {
            return instance.get();
        }

        final T service = adapterLookup.lookup(type, qualifiers)
                .filter(this::isAvailable)
                .findFirst().orElseThrow(() -> new IllegalStateException("No available implementation found for type " + type.getName() + " with qualifiers " + Arrays.toString(qualifiers)));
        cache.set(service);
        return service;
    }

    private boolean isAvailable(T multiVersioned) {
        try {
            return multiVersioned.isAvailable();
        } catch (Exception e) {
            LOGGER.warning("Availability check threw exception for " + multiVersioned.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

}
