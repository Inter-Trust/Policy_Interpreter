package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.policy.handler.Precondition;

/**
 *
 * @author Juanma
 */
public class EngineMatchEvent {

    Precondition precondition;
    EngineMatch match;

    public EngineMatchEvent(Precondition precondition, EngineMatch match) {
        this.precondition = precondition;
        this.match = match;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public EngineMatch getMatch() {
        return match;
    }

    @Override
    public int hashCode() {
        return 33 + (this.match != null ? this.match.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final EngineMatchEvent other = (EngineMatchEvent) obj;
        return (this.match != null) ? this.match.equals(other.match) : (other.match == null);

    }
}
