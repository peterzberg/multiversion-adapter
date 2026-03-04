package dev.zberg.quarkus.multiversion.runtime;

/**
 * Marker interface for multiversioned beans.
 */
public interface Multiversioned {
    /**
     * Indicates whether this version of the adapter is available or not. You can perform some connection checks, some ping calls, ... Whatever you need to check if this adapter is available.
     * <p>
     * If this methods throws an exception, the result is considered as false and the exception is logged. This allows you to throw an exception if the availability check fails, for example if a connection check fails, without having to catch it and return false.
     *
     * @return true, if this version of the adapter is available, false otherwise.
     */
    boolean isAvailable();
}

