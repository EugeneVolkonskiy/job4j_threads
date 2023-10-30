package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ParallelIndexSearchTest {

    @Test
    public void whenSearchElement8ThanExpectedIndex7() {
        Integer[] array = new Integer[]{2, 12, 44, 324, 64, 6, 1, 8, 33, 23};
        assertThat(ParallelIndexSearch.search(array, 8)).isEqualTo(7);
    }

    @Test
    public void whenSearchElement47ThenExpectedIndex18() {
        Integer[] array = new Integer[]{2, 12, 44, 324, 64, 6, 1, 8, 33, 23,
                75, 13, 14, 16, 667, 45, 95, 46, 47, 48};
        assertThat(ParallelIndexSearch.search(array, 47)).isEqualTo(18);
    }

    @Test
    public void whenSearchElementABCThanExpectedIndex13() {
        String[] array = new String[]{"A", "DE", "AC", "DG", "SE", "DF", "QG", "YH", "JK",
                "ZX", "MN", "UY", "ER", "ABC", "CD", "WS", "XC", "MN", "WW", "LP"};
        assertThat(ParallelIndexSearch.search(array, "ABC")).isEqualTo(13);
    }

    @Test
    public void whenElementNotFoundThanExpectedMinus1() {
        String[] array = new String[]{"A", "DE", "AC", "DG", "SE", "DF", "QG", "YH", "JK", "ZX",
                "MN", "UY", "ER", "LK", "CD", "WS", "XC", "MN", "WW", "LP"};
        assertThat(ParallelIndexSearch.search(array, "Test")).isEqualTo(-1);
    }
}