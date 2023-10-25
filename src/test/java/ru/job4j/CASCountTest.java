package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {

    @Test
    public void whenCount10ThenGet10() throws InterruptedException {
        CASCount count = new CASCount();
        Thread thread1 = new Thread(() -> IntStream.range(0, 5).forEach(i -> count.increment()));
        Thread thread2 = new Thread(() -> IntStream.range(0, 5).forEach(i -> count.increment()));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertThat(count.get()).isEqualTo(10);
    }
}