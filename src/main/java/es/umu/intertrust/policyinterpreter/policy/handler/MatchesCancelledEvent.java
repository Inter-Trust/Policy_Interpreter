package es.umu.intertrust.policyinterpreter.policy.handler;

import java.util.List;

/**
 *
 * @author Juanma
 */
public interface MatchesCancelledEvent extends PreconditionFullfillmentEvent {

    public List<Match> getCancelledMatches();

    public List<Match> getActiveMatches();
}
