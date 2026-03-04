package dev.zberg.quarkus.multiversion.runtime.impl;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import dev.zberg.quarkus.multiversion.runtime.Multiversioned;

interface AdapterLookup<T extends Multiversioned> {

    Stream<T> lookup(final Class<T> type, final Annotation... qualifiers);

}
