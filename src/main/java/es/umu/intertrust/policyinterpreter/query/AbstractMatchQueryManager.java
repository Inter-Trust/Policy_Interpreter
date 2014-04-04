package es.umu.intertrust.policyinterpreter.query;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 * @author Juanma
 */
public abstract class AbstractMatchQueryManager<T> implements QueryManager {

    protected <V extends T> List<V> getMatchingElements(List<T> elements, Filter<V> filter) {
        List<V> matchingElements = new ArrayList<V>();
        for (T element : elements) {
            try {
                V castedElement = (V) element;
                if (filter.matches(castedElement)) {
                    matchingElements.add(castedElement);
                }
            } catch (ClassCastException ex) {
            }
        }
        return matchingElements;
    }

    protected <V extends T> V getMatchingElement(List<T> elements, Filter<V> filter) {
        for (T element : elements) {
            try {
                V castedElement = (V) element;
                if (filter.matches(castedElement)) {
                    return castedElement;
                }
            } catch (ClassCastException ex) {
            }
        }
        return null;
    }
}
