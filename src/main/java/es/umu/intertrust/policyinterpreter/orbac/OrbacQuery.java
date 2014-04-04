package es.umu.intertrust.policyinterpreter.orbac;

/**
 *
 * @author Juanma
 */
public class OrbacQuery {

    String subject;
    String action;
    String object;

    public OrbacQuery(String subject, String action, String object) {
        this.subject = subject;
        this.action = action;
        this.object = object;
    }

    public Boolean evaluate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
