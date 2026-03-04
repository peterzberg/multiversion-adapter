package dev.zberg.quarkus.multiversion.it.impl;

import dev.zberg.quarkus.multiversion.it.TestService;

public class NoBeanTestServiceV3Impl implements TestService {
    @Override
    public String getVersion() {
        return "v3";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
