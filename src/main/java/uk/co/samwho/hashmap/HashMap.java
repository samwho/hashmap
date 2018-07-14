package uk.co.samwho.hashmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class HashMap<K, V> implements Map<K, V> {
    private final double loadFactor;
    private final double resizeFactor;
    private int currentCapacity;
    private int size;

    private List<List<Map.Entry<K, V>>> buckets;

    public HashMap() {
        this(16);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, 0.75, 2);
    }

    public HashMap(int initialCapacity, double loadFactor, double resizeFactor) {
        this.loadFactor = loadFactor;
        this.resizeFactor = resizeFactor;

        this.currentCapacity = initialCapacity;

        this.buckets = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            this.buckets.add(new ArrayList<>());
        }

        this.size = 0;
    }

    private static final class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean containsKey(Object key) {
        int hash = key.hashCode();
        List<Map.Entry<K, V>> bucket = buckets.get(hash % buckets.size());

        for (Map.Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(Object value) {
        for (List<Map.Entry<K, V>> bucket : buckets) {
            for (Map.Entry<K, V> entry : bucket) {
                if (entry.getValue().equals(value)) {
                    return true;
                }
            }
        }

        return false;
    }

    public V get(Object key) {
        return get(buckets, key);
    }

    public V put(K key, V value) {
        V ret = put(buckets, key, value);

        synchronized (this) {
            if (ret == null) {
                this.size++;
            }

            if (isResizeNeeded()) {
                resize();
            }
        }

        return ret;
    }

    public V remove(Object key) {
        int hash = key.hashCode();
        List<Map.Entry<K, V>> bucket = buckets.get(hash % buckets.size());

        int i = 0;
        boolean found = false;
        for (Map.Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                found = true;
                break;
            }
            i++;
        }

        if (!found) {
            return null;
        }

        this.size--;
        return bucket.remove(i).getValue();
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    public void clear() {
        this.size = 0;
        buckets.clear();
    }

    public Set<K> keySet() {
        return buckets.stream().flatMap(Collection::stream).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public Collection<V> values() {
        return buckets.stream().flatMap(Collection::stream).map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return buckets.stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private boolean isResizeNeeded() {
        return this.size > (int)(currentCapacity * loadFactor);
    }

    private void resize() {
        int newCapacity = (int)(currentCapacity * resizeFactor);
        List<List<Map.Entry<K, V>>> newBuckets = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(new ArrayList<>());
        }

        for (Map.Entry<K, V> entry : entrySet()) {
            put(newBuckets, entry.getKey(), entry.getValue());
        }

        buckets = newBuckets;
        currentCapacity = newCapacity;
    }

    private static <K, V> V put(List<List<Map.Entry<K, V>>> buckets, K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException("cannot store null values");
        }

        int hash = key.hashCode();
        List<Map.Entry<K, V>> bucket = buckets.get(hash % buckets.size());

        for (Map.Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.setValue(value);
            }
        }

        bucket.add(new Entry<>(key, value));
        return null;
    }

    private static <K, V> V get(List<List<Map.Entry<K, V>>> buckets, Object key) {
        int hash = key.hashCode();
        List<Map.Entry<K, V>> bucket = buckets.get(hash % buckets.size());

        for (Map.Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return null;
    }
}
