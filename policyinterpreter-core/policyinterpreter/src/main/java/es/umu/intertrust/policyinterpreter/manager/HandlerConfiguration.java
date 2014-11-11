package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.Configuration;
import es.umu.intertrust.policyinterpreter.policyhandlers.DeploymentManager;
import es.umu.intertrust.policyinterpreter.policyhandlers.ExceptionManager;
import es.umu.intertrust.policyinterpreter.policyhandlers.InterpreterManager;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerConfiguration;
import es.umu.intertrust.policyinterpreter.policyhandlers.QueryManager;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public class HandlerConfiguration implements PolicyHandlerConfiguration {

    InterpreterManager interpreterManager;
    DeploymentManager deploymentManager;
    Collection<QueryManager> queryManagers;
    ExceptionManager exceptionManager;

    public HandlerConfiguration(InterpreterManager interpreterManager, DeploymentManager deploymentManager, Collection<QueryManager> queryManagers, ExceptionManager exceptionManager) {
        this.interpreterManager = interpreterManager;
        this.deploymentManager = deploymentManager;
        this.queryManagers = queryManagers;
        this.exceptionManager = exceptionManager;
    }

    public File getConfigurationLocation() {
        return Configuration.getInstance().getPolicyHandlersConfLocation();
    }

    public InterpreterManager getInterpreterManager() {
        return interpreterManager;
    }

    public DeploymentManager getDeploymentManager() {
        return deploymentManager;
    }

    public Collection<QueryManager> getQueryManagers() {
        return queryManagers;
    }

    public ExceptionManager getExceptionManager() {
        return exceptionManager;
    }

}
