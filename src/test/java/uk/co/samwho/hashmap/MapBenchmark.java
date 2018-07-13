package uk.co.samwho.hashmap;

import com.google.caliper.Benchmark;

import java.util.Map;

public class MyBenchmark extends Benchmark {
    public void timeMyOperation(int reps) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < reps; i++) {
            map.put(String.valueOf(i), String.valueOf(i));
        }
    }
}
