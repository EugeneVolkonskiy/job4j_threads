package ru.job4j.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelIndexSearch<T> extends RecursiveTask<Integer> {

    private T[] array;
    private T element;
    private final int from;
    private final int to;

    public ParallelIndexSearch(T[] array, int from, int to, T element) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.element = element;
    }

    public int findIndex() {
        int result = -1;
        for (int i = 0; i < array.length; i++) {
            if (element.equals(array[i])) {
                result = i;
                break;
            }
        }
        return result;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return findIndex();
        }
        int mid = (from + to) / 2;
        var left = new ParallelIndexSearch<>(array, 0, mid, element);
        var right = new ParallelIndexSearch<>(array, mid + 1, array.length, element);
        left.fork();
        right.fork();
        return Math.max(left.join(), right.join());
    }

    public static <T> int search(T[] array, T element) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelIndexSearch<>(array, 0, array.length - 1, element));
    }
}
