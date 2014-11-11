package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.List;

/**
 *
 * @author Juanma
 */
public interface DependencyMapper {

    public SecurityFeatureOperator getDependentForMain(List<Obligation> obligations);

    public SecurityFeatureOperator getDependentForDefaultMain();

    public void processMainForDependent(SecurityFeatureOperator dependent, SecurityFeature feature) throws PolicyHandlerException;

}
