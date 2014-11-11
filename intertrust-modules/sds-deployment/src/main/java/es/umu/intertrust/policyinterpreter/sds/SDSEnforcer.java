package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcer;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcingException;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.sds.jms.JMSSDSSender;
import es.umu.intertrust.policyinterpreter.sds.xml.Sds;

/**
 *
 * @author Juanma
 */
public class SDSEnforcer implements DeploymentEnforcer {

    DeploymentManager deploymentManager;

    public void initialize(DeploymentManager deploymentManager) throws DeploymentEnforcingException {
        this.deploymentManager = deploymentManager;
    }

    @Override
    public void enforce() throws DeploymentEnforcingException {
        try {
            SDSGenerator generator = new SDSGenerator(deploymentManager);
            Sds sds = generator.generate();
            SDSSender sender = new JMSSDSSender();
            sender.initialize(deploymentManager.getDeploymentProperties());
            sender.send(sds);
        } catch (SDSSenderException ex) {
            throw new DeploymentEnforcingException(ex.getMessage(), ex);
        }
    }

}
