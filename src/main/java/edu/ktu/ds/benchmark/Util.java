package edu.ktu.ds.benchmark;

import java.util.List;
import java.util.Random;

public class Util {

    static Random generator = new Random();

    static void generateList(List<Float> list, int size) {
        for (int i = 0; i < size; i++) {
            list.add(generator.nextFloat());
        }
    }
    
    static void generateIndexes(int[] indexes, int listSize) {
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = generator.nextInt(listSize);
        }
    }
}
