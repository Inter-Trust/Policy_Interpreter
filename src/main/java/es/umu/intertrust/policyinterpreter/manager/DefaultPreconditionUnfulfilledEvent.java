package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.policy.handler.Match;
import es.umu.intertrust.policyinterpreter.policy.handler.Precondition;
import es.umu.intertrust.policyinterpreter.policy.handler.PreconditionUnfulfilledEvent;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class DefaultPreconditionUnfulfilledEvent implements PreconditionUnfulfilledEvent {

    Precondition precondition;
    List<Match> cancelledMatches;

    public DefaultPreconditionUnfulfilledEvent(Precondition precondition, List<Match> cancelledMatches) {
        this.precondition = precondition;
        this.cancelledMatches = cancelledMatches;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public List<Match> getCancelledMatches() {
        return cancelledMatches;
    }

}
