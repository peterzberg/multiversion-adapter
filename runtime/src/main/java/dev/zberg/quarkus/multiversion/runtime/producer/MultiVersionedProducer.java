package dev.zberg.quarkus.multiversion.runtime.producer;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import dev.zberg.quarkus.multiversion.runtime.Adapter;
import dev.zberg.quarkus.multiversion.runtime.Cache;
import dev.zberg.quarkus.multiversion.runtime.Multiversioned;
import dev.zberg.quarkus.multiversion.runtime.impl.AdapterImpl;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

@Dependent
public class MultiVersionedProducer {

    private static final long DEFAULT_CACHE_VALIDITY_MILLIS = 10_000L;

    @Produces
    @Dependent
    public <T extends Multiversioned> Adapter<T> produce(final InjectionPoint injectionPoint) {
        final Set<Annotation> qualifiers = injectionPoint.getQualifiers();
        final ParameterizedType type = (ParameterizedType) injectionPoint.getType();
        final Class<T> interfaceType = (Class<T>) type.getActualTypeArguments()[0];

        final Cache cacheAnnotation = injectionPoint.getAnnotated().getAnnotation(Cache.class);
        final long cacheValidityInMillis = cacheAnnotation != null ? cacheAnnotation.validityInMillis() : DEFAULT_CACHE_VALIDITY_MILLIS;
        if (cacheValidityInMillis < 0) {
            throw new IllegalArgumentException("Cache validity must be non-negative");
        }

        return new AdapterImpl<>(interfaceType, cacheValidityInMillis, qualifiers.toArray(new Annotation[0]));
    }

}
