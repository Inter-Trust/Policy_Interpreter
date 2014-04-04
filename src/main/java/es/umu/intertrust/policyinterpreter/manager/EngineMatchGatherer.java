package es.umu.intertrust.policyinterpreter.manager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class EngineMatchGatherer implements EngineMatchListener {

    List<EngineMatchEvent> createdMatches;
    List<EngineMatchEvent> cancelledMatches;

    public EngineMatchGatherer() {
        this.createdMatches = new ArrayList<EngineMatchEvent>();
        this.cancelledMatches = new ArrayList<EngineMatchEvent>();
    }

    public void matchCreated(EngineMatchEvent evt) {
        if (cancelledMatches.contains(evt)) {
            cancelledMatches.remove(evt);
        } else {
            createdMatches.add(evt);
        }
    }

    public void matchCancelled(EngineMatchEvent evt) {
        if (createdMatches.contains(evt)) {
            createdMatches.remove(evt);
        } else {
            cancelledMatches.add(evt);
        }
    }

    public List<EngineMatchEvent> getCreatedMatches() {
        return createdMatches;
    }

    public List<EngineMatchEvent> getCancelledMatches() {
        return cancelledMatches;
    }
}
