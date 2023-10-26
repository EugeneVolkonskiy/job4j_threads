package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CountBarrier {

    @GuardedBy("this")
    private final Object monitor = this;
    private final int total;
    private int count = 0;


    public CountBarrier(final int total) {
        this.total = total;
    }

    public synchronized void count() {
            count++;
            monitor.notifyAll();
    }

    public synchronized void await() {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
        }
    }

    public static void main(String[] args) {
        CountBarrier countBarrier = new CountBarrier(5);
        Thread threadAwait = new Thread(countBarrier::await, "Thread-Await");
        threadAwait.start();
        for (int i = 1; i <= countBarrier.total; i++) {
            Thread threadCount = new Thread(countBarrier::count);
            System.out.println(threadCount.getName());
            threadCount.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
