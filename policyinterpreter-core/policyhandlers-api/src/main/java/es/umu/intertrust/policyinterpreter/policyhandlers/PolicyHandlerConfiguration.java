package es.umu.intertrust.policyinterpreter.policyhandlers;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public interface PolicyHandlerConfiguration {

    public File getConfigurationLocation();

    public InterpreterManager getInterpreterManager();

    public DeploymentManager getDeploymentManager();

    public Collection<QueryManager> getQueryManagers();

    public ExceptionManager getExceptionManager();

}
