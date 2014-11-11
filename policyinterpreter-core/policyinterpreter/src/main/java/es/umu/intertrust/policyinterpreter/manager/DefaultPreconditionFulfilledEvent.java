package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionFulfilledEvent;

/**
 *
 * @author Juanma
 */
public class DefaultPreconditionFulfilledEvent implements PreconditionFulfilledEvent {

    Precondition precondition;
    Match match;

    public DefaultPreconditionFulfilledEvent(Precondition precondition, Match match) {
        this.precondition = precondition;
        this.match = match;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public Match getMatch() {
        return match;
    }

}
