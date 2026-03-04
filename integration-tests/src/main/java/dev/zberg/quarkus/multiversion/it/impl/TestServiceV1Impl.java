package dev.zberg.quarkus.multiversion.it.impl;

import dev.zberg.quarkus.multiversion.it.TestService;

import jakarta.enterprise.context.Dependent;

@Dependent
public class TestServiceV1Impl implements TestService {
    @Override
    public String getVersion() {
        return "v1";
    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}
