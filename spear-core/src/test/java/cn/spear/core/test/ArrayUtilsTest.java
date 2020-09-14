package cn.spear.core.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shay
 * @date 2020/9/9
 */
@Slf4j
public class ArrayUtilsTest {

    @Test
    public void shortestPathTest() {
        int[][] grid = new int[][]{
                {0, 0, 0},
                {1, 0, 1},
                {0, 0, 1},
                {0, 1, 1},
                {0, 0, 0}
        };
        List<String> paths = new ArrayList<>();
        int m = grid.length, n = grid[0].length;
        int currentX = 0;
        for (int y = 0; y < m; y++) {
            int[] line = grid[y];
            for (int x = currentX; x < n; x++) {
                int value = line[y];
                if (value == 0) {
                    int nextValue = grid[y + 1][x];
                    if (nextValue == 0) {
                        break;
                    }
                }
            }
        }
        log.info(String.join(",", paths));
    }
}
