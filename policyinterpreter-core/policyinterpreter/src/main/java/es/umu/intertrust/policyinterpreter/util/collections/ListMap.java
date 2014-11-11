package es.umu.intertrust.policyinterpreter.util.collections;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <K>
 * @param <V>
 * @author Juanma
 */
public interface ListMap<K, V> extends Map<K, List<V>> {

    /**
     * Returns the list of values to which the specified key is mapped. If this
     * map contains no mapping for the key, creates a new list and associates it
     * with the specified key.
     *
     * @param key
     * @return the list of values to which the specified key is mapped.
     */
    public List<V> getList(K key);

    public void addToList(K key, V value);

    public void addAllToList(K key, Collection<V> values);

    public void removeFromList(K key, V value);

    public void removeAllFromList(K key, Collection<V> value);

}
