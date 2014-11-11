package es.umu.intertrust.policyinterpreter.policyhandlers;

import es.umu.intertrust.policyinterpreter.preconditions.PreconditionFulfillmentListener;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import java.util.List;

/**
 * Interface to be implemented by plug-ins that handle policies.
 *
 * @author Juanma
 */
public interface PolicyHandler extends PreconditionFulfillmentListener {

    public void initialize(PolicyHandlerConfiguration configuration) throws PolicyHandlerInitializationException;

    public List<Precondition> getPreconditions() throws PolicyHandlerException;

    public InterpreterManager getInterpreterManager();

    public DeploymentManager getDeploymentManager();

    public <T extends QueryManager> T getQueryManager(Class<T> queryManagerClass) throws UnsupportedQueryManagerException;

    public ExceptionManager getExceptionManager();

}
