package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionUnfulfilledEvent;

/**
 *
 * @author Juanma
 */
public class DefaultPreconditionUnfulfilledEvent implements PreconditionUnfulfilledEvent {

    Precondition precondition;
    Match cancelledMatch;

    public DefaultPreconditionUnfulfilledEvent(Precondition precondition, Match cancelledMatch) {
        this.precondition = precondition;
        this.cancelledMatch = cancelledMatch;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public Match getCancelledMatch() {
        return cancelledMatch;
    }

}
