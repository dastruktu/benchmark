package edu.ktu.ds.benchmark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleBenchmark {

    static final int OPERATION_COUNT = 1_000;
    static final int[] LIST_SIZES = {/*64_000,*/ 4_000, 8_000, 16_000, 32_000};

    public static void main(String[] args) {
        runBenchmark();
    }

    static void runBenchmark() {
        int[] indexes = new int[OPERATION_COUNT];
        System.out.format("%1$8s%2$16s%3$16s%n", "", "array list", "linked list");
        for (int listSize : LIST_SIZES) {
            Util.generateIndexes(indexes, listSize);
            long arrayListTime = generateAndRun(new ArrayList<>(), listSize, indexes);
            long linkedListTime = generateAndRun(new LinkedList<>(), listSize, indexes);
            System.out.format("%1$8s%2$16s%3$16s%n", listSize, arrayListTime, linkedListTime);
        }
    }

    static long generateAndRun(List<Float> list, int listSize, int[] indexes) {
        Util.generateList(list, listSize);
        return measureTime(() -> {
            for (int i : indexes) {
                list.get(i);
            }
        }
        );
    }

    static long measureTime(Runnable code) {
        long start = System.nanoTime();
        code.run();
        return System.nanoTime() - start;
    }
}
