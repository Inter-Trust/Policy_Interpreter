package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.policy.handler.Match;
import es.umu.intertrust.policyinterpreter.policy.handler.Precondition;
import es.umu.intertrust.policyinterpreter.policy.handler.PreconditionFulfilledEvent;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class DefaultPreconditionFulfilledEvent implements PreconditionFulfilledEvent {

    Precondition precondition;
    List<Match> matches;

    public DefaultPreconditionFulfilledEvent(Precondition precondition, List<Match> matches) {
        this.precondition = precondition;
        this.matches = matches;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public List<Match> getMatches() {
        return matches;
    }

}
