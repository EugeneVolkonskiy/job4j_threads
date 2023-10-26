package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    public void whenOffer10Poll7ThanCount3() throws InterruptedException {
        var sbq = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> IntStream.range(0, 10).forEach(value -> {
            try {
                sbq.offer(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        Thread consumer = new Thread(() -> IntStream.range(0, 7).forEach(i -> {
            try {
                sbq.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(sbq.getSize()).isEqualTo(3);
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(
                () -> IntStream.range(0, 5).forEach(
                        value -> {
                            try {
                                queue.offer(value);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                )
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (queue.getSize() > 0 || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }
}