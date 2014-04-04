package es.umu.intertrust.policyinterpreter.policy.handler;

import es.umu.intertrust.policyinterpreter.query.QueryManager;
import es.umu.intertrust.policyinterpreter.query.UnsupportedQueryManagerException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface to be implemented by plug-ins that handle policies.
 *
 * @author Juanma
 */
public abstract class PolicyHandler implements PreconditionFulfillmentListener {

    DeploymentManager deploymentManager;
    Map<Class, QueryManager> queryManagers;
    PolicyHandlerStorage storage;

    public PolicyHandler() {
        this.storage = new PolicyHandlerStorage();
    }

    public DeploymentManager getDeploymentManager() {
        return deploymentManager;
    }

    public void setDeploymentManager(DeploymentManager deploymentManager) {
        this.deploymentManager = deploymentManager;
    }

    public void setQueryManagers(Collection<QueryManager> queryManagers) {
        this.queryManagers = new HashMap<Class, QueryManager>();
        for (QueryManager queryManager : queryManagers) {
            this.queryManagers.put(queryManager.getClass(), queryManager);
        }
    }

    public <T extends QueryManager> T getQueryManager(Class<T> queryManagerClass) throws UnsupportedQueryManagerException {
        QueryManager queryManager = queryManagers.get(queryManagerClass);
        if (queryManager == null) {
            throw new UnsupportedQueryManagerException();
        }
        return (T) queryManager;
    }

    public void setManagers(Collection<QueryManager> queryManagers, DeploymentManager deploymentManager) {
        setQueryManagers(queryManagers);
        setDeploymentManager(deploymentManager);
    }

    public PolicyHandlerStorage getStorage() {
        return storage;
    }

    public void fulfilledPreconditionMatchesCreated(MatchesCreatedEvent evt) {
        // Default empty implementation for policy handlers not interested in this event.
    }

    public void fulfilledPreconditionMatchesCancelled(MatchesCancelledEvent evt) {
        // Default empty implementation for policy handlers not interested in this event.
    }

    public abstract List<Precondition> getPreconditions();
}
