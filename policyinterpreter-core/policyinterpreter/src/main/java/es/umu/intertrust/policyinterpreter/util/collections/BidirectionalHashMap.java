package es.umu.intertrust.policyinterpreter.util.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Juanma
 * @param <K>
 * @param <V>
 */
public class BidirectionalHashMap<K, V> implements BidirectionalMap<K, V> {

    Map<K, V> directMap;
    Map<V, K> inverseMap;

    public BidirectionalHashMap() {
        directMap = new HashMap<K, V>();
        inverseMap = new HashMap<V, K>();
    }

    public int size() {
        return directMap.size();
    }

    public boolean isEmpty() {
        return directMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return directMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return directMap.containsValue(value);
    }

    public V get(Object key) {
        return directMap.get(key);
    }

    public K getByValue(V value) {
        return inverseMap.get(value);
    }

    public V put(K key, V value) {
        V v = directMap.put(key, value);
        inverseMap.put(value, key);
        return v;
    }

    public V remove(Object key) {
        V v = directMap.remove(key);
        if (v != null) {
            inverseMap.remove(v);
        }
        return v;
    }

    public K removeByValue(V value) {
        K k = inverseMap.remove(value);
        if (k != null) {
            directMap.remove(k);
        }
        return k;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        directMap.putAll(m);
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            inverseMap.put(entry.getValue(), entry.getKey());
        }
    }

    public void clear() {
        directMap.clear();
        inverseMap.clear();
    }

    public Set<K> keySet() {
        return directMap.keySet();
    }

    public Collection<V> values() {
        return directMap.values();
    }

    public Set<Entry<K, V>> entrySet() {
        return directMap.entrySet();
    }

    public Set<Entry<V, K>> entrySetByValue() {
        return inverseMap.entrySet();
    }

}
