package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcer;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcingException;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.dummy.DummySDSSender;

/**
 *
 * @author Juanma
 */
public class SDSEnforcer implements DeploymentEnforcer {

    public void enforce(DeploymentManager manager) throws DeploymentEnforcingException {
        SDSGenerator generator = new SDSGenerator();
        SDSDocument sds = generator.generate(manager.getAddedFeatures(), manager.getRemovedFeatures());
        new DummySDSSender().send(sds);
    }

}
