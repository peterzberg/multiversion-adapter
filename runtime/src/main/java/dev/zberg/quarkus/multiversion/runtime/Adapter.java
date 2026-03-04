package dev.zberg.quarkus.multiversion.runtime;

/**
 * Adapter interface that holds multiple versions of an interface.
 *
 * @param <T> The service interface type that extends {@link Multiversioned}.
 */
public interface Adapter<T extends Multiversioned> {

    /**
     * Gets a service that implements {@code T}. If there are multiple implementations of {@code T} available, the one that is currently available will be returned. If no implementation is available, an {@link IllegalStateException} will be thrown.
     * <p>
     * The result will be cached for 10 seconds. So availability checks will be performed at most every 10 seconds. This allows you to perform some expensive checks in the {@link Multiversioned#isAvailable()} method without impacting the performance of your application too much. You can also use this caching mechanism to avoid performing availability checks too often if they are not necessary.
     *
     * @return An instance of the service interface that is currently available.
     * @throws IllegalStateException if no available implementation is found.
     */
    T get();
}
