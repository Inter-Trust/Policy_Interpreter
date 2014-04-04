package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.policy.handler.Match;
import es.umu.intertrust.policyinterpreter.policy.handler.MatchesCreatedEvent;
import es.umu.intertrust.policyinterpreter.policy.handler.Precondition;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class DefaultMatchesCreatedEvent implements MatchesCreatedEvent {

    Precondition precondition;
    List<Match> createdMatches;
    List<Match> oldMatches;

    public DefaultMatchesCreatedEvent(Precondition precondition, List<Match> createdMatches, List<Match> oldMatches) {
        this.precondition = precondition;
        this.createdMatches = createdMatches;
        this.oldMatches = oldMatches;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public List<Match> getCreatedMatches() {
        return createdMatches;
    }

    public List<Match> getOldMatches() {
        return oldMatches;
    }

}
