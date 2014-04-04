package es.umu.intertrust.policyinterpreter.orbac;

import es.umu.intertrust.policyinterpreter.policy.PolicyRule;

/**
 *
 * @author Juanma
 */
public class Obligation extends PolicyRule {

    String name;
    String subject;
    String action;
    String object;

    public Obligation(String name, String subject, String action, String object) {
        this.name = name;
        this.subject = subject;
        this.action = action;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public int hashCode() {
        return 415 + (this.name != null ? this.name.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Obligation other = (Obligation) obj;
        return (this.name != null) ? this.name.equals(other.name) : (other.name == null);
    }

}
