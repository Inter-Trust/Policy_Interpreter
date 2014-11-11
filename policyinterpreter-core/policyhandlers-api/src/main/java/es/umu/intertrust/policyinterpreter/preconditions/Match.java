package es.umu.intertrust.policyinterpreter.preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class Match {

    Map<String, Object> declaredMatchingValues;
    Collection<Object> matchingValues;

    public Match(Map<String, Object> declaredMatchingValues, Collection<Object> matchingValues) {
        this.declaredMatchingValues = declaredMatchingValues;
        this.matchingValues = matchingValues;
    }

    public Collection<String> getDeclarationIds() {
        return declaredMatchingValues.keySet();
    }

    public Object getMatchingValue(String id) {
        return declaredMatchingValues.get(id);
    }

    public Collection<Object> getMatchingValues() {
        return matchingValues;
    }

    public <T> T getMatchingValue(Class<T> targetClass) {
        for (Object matchingValue : matchingValues) {
            if (targetClass.isInstance(matchingValue)) {
                return (T) matchingValue;
            }
        }
        return null;
    }

    public <T> Collection<T> getMatchingValues(Class<T> targetClass) {
        Collection<T> classMatchingValues = new ArrayList<T>();
        for (Object matchingValue : matchingValues) {
            if (targetClass.isInstance(matchingValue)) {
                classMatchingValues.add((T) matchingValue);
            }
        }
        return classMatchingValues;
    }

    @Override
    public String toString() {
        return "Match{" + "declaredMatchingValues=" + declaredMatchingValues + ", matchingValues=" + matchingValues + '}';
    }
}
