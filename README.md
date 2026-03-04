# Multiversion Adapter

## Problem

API changes can lead to compatibility issues, especially when they occur suddenly.
For example, it may be announced that a new version v2 of an API will soon be available, but without any lead time.
At point X, the switch happens. This means you need to be able to handle multiple versions.

Normally, this should not be a problem. In a perfect world, the provider of the API should provide v1 and v2 at the same time, and clients can choose which version to use and plan a smooth migration.
However, in the real world, this is not always the case. Sometimes, the provider may only provide v2 and immediately deprecate v1, leaving clients with no choice but to switch to v2 immediately.

## Solution

The Multiversion Adapter Pattern enables support for multiple versions of an API simultaneously by providing an adapter layer between the different API versions and the clients.
If a version is no longer available, it is simply no longer used.

## Example

```java
public interface TestService extends Multiversioned {

    String performSomeAction();
}
```

```java
@Dependent
public class TestServiceV1Impl implements TestService {
    @Override
    public String performSomeAction() {
        return "some action performed by v1";
    }

    @Override
    public boolean isAvailable() {
        // connect to server xy and check if v1 is available.
        return true;
    }
}
```

```java
@Dependent
public class TestServiceV2Impl implements TestService {
    @Override
    public String performSomeAction() {
        return "some action performed by v2";
    }

    @Override
    public boolean isAvailable() {
        // connect to server xy and check if v2 is available
        return false; // server is not available
    }
}
```

```java
@ApplicationScoped
public class SomeServiceAdapter implements SomeServicePort {

    private final Adapter<TestService> adapter; // adapter for all implementations of TestService

    @Inject
    public MultiversionResource(@Cache(validityInMillis = 10_000L) final Adapter<TestService> adapter) {
        this.adapter = adapter;
    }
    
    @Override
    public String performSomeAction() {
        return "Hello multiversion. I am version " + adapter.get().performSomeAction();
    }
}
```
The annotation `@Cache` is optional. If not given, the adapter will set a default validity of 10 seconds. After this, a new lookup gets performed and a new adapter version may be chosen.
