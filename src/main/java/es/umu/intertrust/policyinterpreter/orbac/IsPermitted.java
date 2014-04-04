package es.umu.intertrust.policyinterpreter.orbac;

/**
 *
 * @author Juanma
 */
public class IsPermitted extends OrbacQuery {

    public IsPermitted(String subject, String action, String object) {
        super(subject, action, object);
    }

}
