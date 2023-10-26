package ru.job4j.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheTest {

    @Test
    public void whenAdd() {
        Cache cache = new Cache();
        Base base = new Base(1, 1);
        cache.add(base);
        assertThat(cache.get(1)).isEqualTo(base);
    }

    @Test
    public void whenUpdate() {
        Cache cache = new Cache();
        Base base1 = new Base(1, 1);
        base1.setName("First");
        cache.add(base1);
        Base base2 = new Base(1, 1);
        base2.setName("Second");
        cache.update(base2);
        assertThat(cache.get(1).getVersion()).isEqualTo(2);
        assertThat(cache.get(1).getName()).isEqualTo("Second");
    }

    @Test
    public void whenExpectedOptimisticException() {
        Cache cache = new Cache();
        Base base1 = new Base(1, 1);
        base1.setName("First");
        cache.add(base1);
        Base base2 = new Base(1, 2);
        base2.setName("Second");
        OptimisticException exception = assertThrows(OptimisticException.class,
                () -> cache.update(base2));
        assertTrue(exception.getMessage().contains("Versions are not equal"));
    }

    @Test
    public void whenDelete() {
        Cache cache = new Cache();
        Base base1 = new Base(1, 1);
        Base base2 = new Base(2, 1);
        cache.add(base1);
        cache.add(base2);
        cache.delete(base1);
        assertThat(cache.get(1)).isNull();
    }
}