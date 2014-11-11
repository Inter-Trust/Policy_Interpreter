package es.umu.intertrust.policyinterpreter.query;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @param <T>
 * @author Juanma
 */
public abstract class AbstractAndFilter<T> implements Filter<T> {

    List<Filter<T>> filters;

    public AbstractAndFilter(Filter<T>... filters) {
        Arrays.asList(filters);
    }

    public boolean matches(T element) {
        for (Filter<T> filter : filters) {
            if (!filter.matches(element)) {
                return false;
            }
        }
        return true;
    }

}
