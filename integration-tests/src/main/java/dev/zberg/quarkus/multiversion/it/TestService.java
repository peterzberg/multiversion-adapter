package dev.zberg.quarkus.multiversion.it;

import dev.zberg.quarkus.multiversion.runtime.Multiversioned;

public interface TestService extends Multiversioned {

    String getVersion();
}
