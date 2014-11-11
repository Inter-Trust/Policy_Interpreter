package es.umu.intertrust.policyinterpreter.orbac;

import es.umu.intertrust.policyinterpreter.preconditions.Precondition;

/**
 *
 * @author Juanma
 */
public class ObligationPrecondition extends Precondition {

    String subject;
    String action;
    String object;
    String parameter;

    public ObligationPrecondition(String action) {
        this(null, action, null, null);
    }

    public ObligationPrecondition(String action, String object) {
        this(null, action, object, null);
    }

    public ObligationPrecondition(String subject, String action, String object) {
        this(subject, action, object, null);
    }

    public ObligationPrecondition(String subject, String action, String object, String parameter) {
        super("Obligation ("
                + ((subject != null) ? "subject == \"" + subject + "\"" : "")
                + ((action != null) ? ((subject != null) ? ", " : "") + "action == \"" + action + "\"" : "")
                + ((object != null) ? ((subject != null) || (action != null) ? ", " : "") + "object == \"" + object + "\"" : "")
                + ((parameter != null) ? ((subject != null) || (action != null) || (object != null) ? ", " : "") + "parameter == \"" + parameter + "\"" : "")
                + ")");

        this.subject = subject;
        this.action = action;
        this.object = object;
        this.parameter = parameter;
    }

    public String getSubject() {
        return subject;
    }

    public String getAction() {
        return action;
    }

    public String getObject() {
        return object;
    }

    public String getParameter() {
        return parameter;
    }

}
