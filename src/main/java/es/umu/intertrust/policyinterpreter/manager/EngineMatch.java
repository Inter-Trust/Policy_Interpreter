package es.umu.intertrust.policyinterpreter.manager;

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

}
