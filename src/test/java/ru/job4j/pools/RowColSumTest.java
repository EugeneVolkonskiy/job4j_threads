package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;
import static ru.job4j.pools.RowColSum.asyncSum;
import static ru.job4j.pools.RowColSum.sum;

class RowColSumTest {

    @Test
    public void whenColumn0SumThenResult12() {
        int[][] array = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RowColSum.Sums[] res = sum(array);
        assertThat(res[0].colSum()).isEqualTo(12);
    }

    @Test
    public void whenRow1SumThenResult12() {
        int[][] array = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RowColSum.Sums[] res = sum(array);
        assertThat(res[1].rowSum()).isEqualTo(15);
    }

    @Test
    public void whenColumn2SumThenResult18() throws ExecutionException, InterruptedException {
        int[][] array = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RowColSum.Sums[] res = asyncSum(array);
        assertThat(res[2].colSum()).isEqualTo(18);
    }

    @Test
    public void whenRow2SumThenResult24() throws ExecutionException, InterruptedException {
        int[][] array = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RowColSum.Sums[] res = asyncSum(array);
        assertThat(res[2].rowSum()).isEqualTo(24);
    }
}