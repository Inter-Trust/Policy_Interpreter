package es.umu.intertrust.policyinterpreter.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @param <K>
 * @param <V>
 * @author Juanma
 */
public class ListHashMap<K, V> extends HashMap<K, List<V>> implements ListMap<K, V> {

    public List<V> getList(K key) {
        List<V> list = get(key);
        if (list == null) {
            list = new ArrayList<V>();
            put(key, list);
        }
        return list;
    }

    public void addToList(K key, V value) {
        List<V> list = getList(key);
        list.add(value);
    }

    public void addAllToList(K key, Collection<V> values) {
        List<V> list = getList(key);
        list.addAll(values);
    }

    public void removeFromList(K key, V value) {
        List<V> list = get(key);
        if (list != null) {
            list.remove(value);
            if (list.isEmpty()) {
                remove(key);
            }
        }
    }

    public void removeAllFromList(K key, Collection<V> value) {
        List<V> list = get(key);
        if (list != null) {
            list.removeAll(value);
            if (list.isEmpty()) {
                remove(key);
            }
        }
    }

}
