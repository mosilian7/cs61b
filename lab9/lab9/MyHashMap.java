package lab9;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V>, Iterable<K> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    private MyHashMap(int bucketSize) {
        buckets = new ArrayMap[bucketSize];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int index = hash(key);
        return buckets[index].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (buckets[index].get(key) == null) {
            size += 1;
        }
        buckets[index].put(key,value);
        resize();
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    private void resize() {
        if (loadFactor()>MAX_LF) {
            MyHashMap<K,V> update = new MyHashMap<>(2*buckets.length);
            for (K key:this) {
                update.put(key,this.get(key));
            }
            this.buckets = update.buckets;
        }
    }
    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new keyIterator();
    }

    private class keyIterator implements Iterator<K>{
        private Iterator<ArrayMap<K, V>> bigIterator;
        private Iterator<K> smallIterator;
        public keyIterator() {
            bigIterator = Arrays.stream(buckets).iterator();
        }
        public boolean hasNext() {
            return (bigIterator.hasNext()) || (smallIterator.hasNext());
        }
        public K next() {
            if (smallIterator == null || !smallIterator.hasNext()) {
                smallIterator = bigIterator.next().iterator();
                return next();
            }
            return smallIterator.next();
        }
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> m = new MyHashMap<>();
        for (int i = 0; i < 5; i++) {
            m.put("hi" + i, 1 + i);
            m.get("hi"+i);
        }
        for (String key:m) {
            System.out.println(key);
        }
    }
}
