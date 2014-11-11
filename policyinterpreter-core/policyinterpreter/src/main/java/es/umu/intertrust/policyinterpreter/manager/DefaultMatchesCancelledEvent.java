package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.MatchesCancelledEvent;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class DefaultMatchesCancelledEvent implements MatchesCancelledEvent {

    Precondition precondition;
    List<Match> cancelledMatches;
    List<Match> activeMatches;

    public DefaultMatchesCancelledEvent(Precondition precondition, List<Match> cancelledMatches, List<Match> activeMatches) {
        this.precondition = precondition;
        this.cancelledMatches = cancelledMatches;
        this.activeMatches = activeMatches;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public List<Match> getCancelledMatches() {
        return cancelledMatches;
    }

    public List<Match> getActiveMatches() {
        return activeMatches;
    }

}
