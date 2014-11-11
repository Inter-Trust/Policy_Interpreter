package es.umu.intertrust.policyinterpreter.sds.filters;

import es.umu.intertrust.policyinterpreter.query.AbstractAndFilter;
import es.umu.intertrust.policyinterpreter.query.Filter;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;

/**
 *
 * @author Juanma
 */
public class AndFilter extends AbstractAndFilter<SecurityFeature> {

    public AndFilter(Filter<SecurityFeature>... filters) {
        super(filters);
    }

}
