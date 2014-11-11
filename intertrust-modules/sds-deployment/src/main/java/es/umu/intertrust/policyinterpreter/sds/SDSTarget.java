package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.Target;

/**
 *
 * @author Juanma
 */
public class SDSTarget implements Target {

    String id;

    public SDSTarget(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

}
