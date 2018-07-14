package uk.co.samwho.hashmap;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    private static final int N = 10_000;

    public static void main(String... args) throws IOException {
        System.out.println("pid: " +ProcessHandle.current().pid());
        System.in.read();
        System.out.println("starting...");

        myHashmap();
        javaUtilHashmap();
        treeMap();
    }

    private static void myHashmap() {
        exerciseMap(new HashMap<>());
    }

    private static void javaUtilHashmap() {
        exerciseMap(new java.util.HashMap<>());
    }

    private static void treeMap() {
        exerciseMap(new TreeMap<>());
    }

    private static void exerciseMap(Map<Integer, Integer> map) {
        for (int i = 0; i < N; i++) {
            map.put(i, i);
        }

        for (int i = 0; i < N; i++) {
            map.put(i, map.get(i));
        }

        for (int i = 0; i < N; i++) {
            map.remove(i);
        }

        System.out.println(map.size());
    }
}
