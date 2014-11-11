package es.umu.intertrust.policyinterpreter.util.collections;

import java.util.Map;
import java.util.Set;

/**
 *
 * @param <K>
 * @param <V>
 * @author Juanma
 */
public interface BidirectionalMap<K, V> extends Map<K, V> {

    public K getByValue(V value);

    public K removeByValue(V value);

    public Set<Entry<V, K>> entrySetByValue();
}
