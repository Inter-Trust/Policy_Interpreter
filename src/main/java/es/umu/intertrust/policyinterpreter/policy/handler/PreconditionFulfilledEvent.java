package es.umu.intertrust.policyinterpreter.policy.handler;

import java.util.List;

/**
 *
 * @author Juanma
 */
public interface PreconditionFulfilledEvent extends PreconditionFullfillmentEvent {

    public List<Match> getMatches();
}
