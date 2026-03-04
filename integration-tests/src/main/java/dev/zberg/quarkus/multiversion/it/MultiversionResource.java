package dev.zberg.quarkus.multiversion.it;

import dev.zberg.quarkus.multiversion.runtime.Adapter;
import dev.zberg.quarkus.multiversion.runtime.Cache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/multiversion")
@ApplicationScoped
public class MultiversionResource {

    private final Adapter<TestService> adapter;

    @Inject
    public MultiversionResource(@Cache(validityInMillis = 2_000) final Adapter<TestService> adapter) {
        this.adapter = adapter;
    }

    @GET
    public String hello() {
        return "Hello multiversion. I am version " + adapter.get().getVersion();
    }
}
