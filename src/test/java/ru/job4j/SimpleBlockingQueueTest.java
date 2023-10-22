package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    public void whenOffer10Poll7ThanCount3() throws InterruptedException {
        var sbq = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> IntStream.range(0, 10).forEach(sbq::offer));
        Thread consumer = new Thread(() -> IntStream.range(0, 7).forEach(i -> sbq.poll()));
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(sbq.getCount()).isEqualTo(3);
    }
}