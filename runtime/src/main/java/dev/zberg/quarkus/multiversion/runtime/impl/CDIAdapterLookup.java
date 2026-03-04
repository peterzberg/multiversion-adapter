package dev.zberg.quarkus.multiversion.runtime.impl;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import dev.zberg.quarkus.multiversion.runtime.Multiversioned;
import io.quarkus.arc.Arc;

class CDIAdapterLookup<T extends Multiversioned> implements AdapterLookup<T> {
    @Override
    public Stream<T> lookup(Class<T> type, Annotation... qualifiers) {
        return Arc.container().select(type, qualifiers).stream();
    }
}
