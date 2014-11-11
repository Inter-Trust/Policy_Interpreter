package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandler;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class PreconditionData {

    PolicyHandler handler;
    List<EngineMatch> matches;

    public PreconditionData(PolicyHandler handler) {
        this.handler = handler;
        this.matches = new ArrayList<EngineMatch>();
    }

    public PolicyHandler getHandler() {
        return handler;
    }

    public void addMatch(EngineMatch match) {
        this.matches.add(match);
    }

    public void addMatches(List<EngineMatch> matches) {
        this.matches.addAll(matches);
    }

    public void removeMatch(EngineMatch match) {
        this.matches.remove(match);
    }

    public void removeMatches(List<EngineMatch> matches) {
        this.matches.removeAll(matches);
    }

    public List<EngineMatch> getMatches() {
        return matches;
    }

    public boolean isPreconditionFulfilled() {
        return !matches.isEmpty();
    }
}
