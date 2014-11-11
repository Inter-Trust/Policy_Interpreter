package es.umu.intertrust.policyinterpreter.preconditions;

import java.util.List;

/**
 *
 * @author Juanma
 */
public interface MatchesCreatedEvent extends PreconditionFullfillmentEvent {

    public List<Match> getCreatedMatches();

    public List<Match> getOldMatches();
}
