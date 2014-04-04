package es.umu.intertrust.policyinterpreter;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcer;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.dummy.DummyPolicyNotificationHandler;
import es.umu.intertrust.policyinterpreter.manager.InterpreterManager;
import es.umu.intertrust.policyinterpreter.policy.PolicyManager;
import es.umu.intertrust.policyinterpreter.policy.PolicyNotificationHandler;
import es.umu.intertrust.policyinterpreter.query.DefaultDeploymentQueryManager;
import es.umu.intertrust.policyinterpreter.query.QueryManager;
import es.umu.intertrust.policyinterpreter.sds.SDSEnforcer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class PolicyInterpreter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Policy and deployment specifics (handler + enforcer)
        PolicyNotificationHandler notificationHandler = new DummyPolicyNotificationHandler();
        DeploymentEnforcer enforcer = new SDSEnforcer();

        // Generics
        PolicyManager policyManager = new PolicyManager();
        notificationHandler.addPolicyChangeListener(policyManager);
        DeploymentManager deploymentManager = new DeploymentManager(enforcer);
        InterpreterManager interpreterManager = new InterpreterManager(policyManager, deploymentManager);
        List<QueryManager> queryManagers = new ArrayList<QueryManager>();

        // Policy and deployment specifics (query managers)
//        queryManagers.add(new OrbacQueryManager());
        queryManagers.add(new DefaultDeploymentQueryManager(deploymentManager));

        // Generics
        interpreterManager.loadPolicyHandlers(queryManagers);

        notificationHandler.start();
    }

}
