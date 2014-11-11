package es.umu.intertrust.policyinterpreter.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.kie.api.runtime.rule.Match;

/**
 *
 * @author Juanma
 */
public class EngineMatch {

    Match match;

    public EngineMatch(Match match) {
        this.match = match;
    }

    public String getRuleName() {
        return this.match.getRule().getName();
    }

    public Map<String, Object> getDeclaredMatchingValues() {
        Map<String, Object> declaredMatchingValues = new HashMap<String, Object>();
        for (String id : match.getDeclarationIds()) {
            declaredMatchingValues.put(id, match.getDeclarationValue(id));
        }
        return declaredMatchingValues;
    }

    public Collection<Object> getMatchingValues() {
        return match.getObjects();
    }
}
