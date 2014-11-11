package es.umu.intertrust.policyinterpreter.manager;

/**
 *
 * @author Juanma
 */
public interface EngineMatchListener {

    public void matchCreated(EngineMatchEvent evt);

    public void matchCancelled(EngineMatchEvent evt);
}
