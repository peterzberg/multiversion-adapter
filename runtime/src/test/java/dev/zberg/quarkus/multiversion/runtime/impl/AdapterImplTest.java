package dev.zberg.quarkus.multiversion.runtime.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import dev.zberg.quarkus.multiversion.runtime.Multiversioned;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdapterImplTest {

    @Test
    void shouldCacheResultForSubsequentCalls() {
        final TestAdapterLookup testLookup = new TestAdapterLookup(new TestServiceV1());
        final AdapterImpl<TestService> testee = new AdapterImpl<>(testLookup, TestService.class, 1000L);

        final TestService service1 = testee.get();
        final TestService service2 = testee.get();

        assertEquals(1, testLookup.getLookupCount());
        assertSame(service1, service2);
    }

    @Test
    void shouldPerformOtherLookupAfterCacheInvalidates() {
        final TestAdapterLookup testLookup = new TestAdapterLookup(new TestServiceV1());
        final AdapterImpl<TestService> testee = new AdapterImpl<>(testLookup, TestService.class, 10L);

        testee.get();
        sleep(20L);
        testee.get();

        assertEquals(2, testLookup.getLookupCount());
    }

    @Test
    void shouldIgnoreExceptionInIsAvailable() {
        TestServiceV1WithException serviceV1WithException = new TestServiceV1WithException();
        final TestAdapterLookup testLookup = new TestAdapterLookup(serviceV1WithException, new TestServiceV1());
        final AdapterImpl<TestService> testee = new AdapterImpl<>(testLookup, TestService.class, 10L);

        final TestService actual = testee.get();

        assertInstanceOf(TestServiceV1.class, actual);
        assertTrue(serviceV1WithException.called);
    }

    private static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldThrowExceptionIfNoAvailableImplementationFound() {
        final TestAdapterLookup testLookup = new TestAdapterLookup();
        final AdapterImpl<TestService> testee = new AdapterImpl<>(testLookup, TestService.class, 1000L);

        final IllegalStateException exception = assertThrows(IllegalStateException.class, testee::get);
        final String expectedMessage = "No available implementation found for type dev.zberg.quarkus.multiversion.runtime.impl.AdapterImplTest$TestService with qualifiers []";
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static class TestAdapterLookup implements AdapterLookup<TestService> {
        private int lookupCount = 0;

        private final List<TestService> implementations;

        public TestAdapterLookup(TestService... implementations) {
            this.implementations = Arrays.asList(implementations);
        }

        @Override
        public Stream<TestService> lookup(Class<TestService> type, java.lang.annotation.Annotation... qualifiers) {
            lookupCount++;
            return this.implementations.stream();
        }

        public int getLookupCount() {
            return lookupCount;
        }
    }

    private interface TestService extends Multiversioned {
        //
    }

    private static class TestServiceV1 implements TestService {
        @Override
        public boolean isAvailable() {
            return true;
        }
    }

    private static class TestServiceV1WithException implements TestService {
        private boolean called;

        @Override
        public boolean isAvailable() {
            called = true;
            throw new IllegalStateException("test");
        }

    }
}