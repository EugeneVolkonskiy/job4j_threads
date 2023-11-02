package ru.job4j.pools;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RowColSum {

    public static record Sums(int rowSum, int colSum) {
    }

    public static Sums[] sum(int[][] matrix) {
        Sums[] result = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < matrix[0].length; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            result[i] = new Sums(rowSum, colSum);
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        CompletableFuture<Sums>[] futures = new CompletableFuture[matrix.length];
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            futures[i] = sumsCount(matrix, i);
        }
        for (int i = 0; i < matrix.length; i++) {
            sums[i] = futures[i].get();
        }
        return sums;
    }

    private static CompletableFuture<Sums> sumsCount(int[][] matrix, int i) {
        return CompletableFuture.supplyAsync(() -> {
            int rowSum = 0;
            int columnSum = 0;
            for (int j = 0; j < matrix.length; j++) {
                rowSum += matrix[i][j];
                columnSum += matrix[j][i];
            }
            return new Sums(rowSum, columnSum);
        });
    }
}
