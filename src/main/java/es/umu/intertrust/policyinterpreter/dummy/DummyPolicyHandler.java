package es.umu.intertrust.policyinterpreter.dummy;

import es.umu.intertrust.policyinterpreter.policy.handler.PolicyHandler;
import es.umu.intertrust.policyinterpreter.policy.handler.Precondition;
import es.umu.intertrust.policyinterpreter.policy.handler.PreconditionFulfilledEvent;
import es.umu.intertrust.policyinterpreter.policy.handler.PreconditionUnfulfilledEvent;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class DummyPolicyHandler extends PolicyHandler {

    @Override
    public List<Precondition> getPreconditions() {
        return Arrays.asList(new Precondition[]{new Precondition("Obligation (action==\"action\")")});
    }

    public void preconditionFulfilled(PreconditionFulfilledEvent evt) {
        SecurityFeature securityFeature = new SecurityFeature();
        getDeploymentManager().addDeploymentFeature(securityFeature);
        getStorage().setDeploymentFeatureOfPrecondition(securityFeature, evt.getPrecondition());
    }

    public void preconditionUnfulfilled(PreconditionUnfulfilledEvent evt) {
        SecurityFeature securityFeature = (SecurityFeature) getStorage().getDeploymentFeaturesByPrecondition(evt.getPrecondition()).iterator().next();
        getDeploymentManager().removeDeploymentFeature(securityFeature);
    }

}
