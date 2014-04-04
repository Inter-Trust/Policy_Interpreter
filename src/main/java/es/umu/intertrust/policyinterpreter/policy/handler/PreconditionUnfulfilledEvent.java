package es.umu.intertrust.policyinterpreter.policy.handler;

import java.util.List;

/**
 *
 * @author Juanma
 */
public interface PreconditionUnfulfilledEvent extends PreconditionFullfillmentEvent {

    public List<Match> getCancelledMatches();
}
