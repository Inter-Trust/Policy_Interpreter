package es.umu.intertrust.policyinterpreter.query;

/**
 *
 * @param <T>
 * @author Juanma
 */
public interface Filter<T> {

    boolean matches(T element);
}
