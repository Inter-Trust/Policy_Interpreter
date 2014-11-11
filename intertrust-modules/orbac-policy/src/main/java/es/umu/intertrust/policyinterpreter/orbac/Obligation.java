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
    String parameter;

    public Obligation(String name, String subject, String action, String object) {
        this(name, subject, action, object, null);
    }

    public Obligation(String name, String subject, String action, String object, String parameter) {
        this.name = name;
        this.subject = subject;
        this.action = action;
        this.object = object;
        this.parameter = parameter;
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

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.subject != null ? this.subject.hashCode() : 0);
        hash = 79 * hash + (this.action != null ? this.action.hashCode() : 0);
        hash = 79 * hash + (this.object != null ? this.object.hashCode() : 0);
        hash = 79 * hash + (this.parameter != null ? this.parameter.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Obligation other = (Obligation) obj;
        if ((this.subject == null) ? (other.subject != null) : !this.subject.equals(other.subject)) {
            return false;
        }
        if ((this.action == null) ? (other.action != null) : !this.action.equals(other.action)) {
            return false;
        }
        if ((this.object == null) ? (other.object != null) : !this.object.equals(other.object)) {
            return false;
        }
        if ((this.parameter == null) ? (other.parameter != null) : !this.parameter.equals(other.parameter)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String string = "Obligation <" + name + "> (" + subject + ", " + action;
        if (parameter != null) {
            string += "." + parameter;
        }
        string += ", " + object + ")";
        return string;
    }

}
