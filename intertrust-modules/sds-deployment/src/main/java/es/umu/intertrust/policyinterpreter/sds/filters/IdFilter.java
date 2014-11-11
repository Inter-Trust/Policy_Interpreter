package es.umu.intertrust.policyinterpreter.sds.filters;

import es.umu.intertrust.policyinterpreter.query.Filter;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;

/**
 *
 * @author Juanma
 */
public class IdFilter implements Filter<SecurityFeature> {

    String id;

    public IdFilter(String id) {
        this.id = id;
    }

    @Override
    public boolean matches(SecurityFeature element) {
        return element.getId().equals(id);
    }

}
