package es.umu.intertrust.policyinterpreter.policyhandlers;

import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.MatchesCancelledEvent;
import es.umu.intertrust.policyinterpreter.preconditions.MatchesCreatedEvent;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionFulfilledEvent;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionUnfulfilledEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public abstract class AbstractPolicyHandler implements PolicyHandler {

    static final String DEFAULT_CONFIGURATION_FILE_NAME_SUFFIX = ".xml";

    PolicyHandlerConfiguration configuration;
    InterpreterManager interpreterManager;
    DeploymentManager deploymentManager;
    Map<Class, QueryManager> queryManagers;
    ExceptionManager exceptionManager;

    @Override
    public void initialize(PolicyHandlerConfiguration configuration) throws PolicyHandlerInitializationException {
        this.configuration = configuration;
        this.interpreterManager = configuration.getInterpreterManager();
        this.deploymentManager = configuration.getDeploymentManager();
        this.queryManagers = new HashMap<Class, QueryManager>();
        for (QueryManager queryManager : configuration.getQueryManagers()) {
            this.queryManagers.put(queryManager.getClass(), queryManager);
        }
        this.exceptionManager = configuration.getExceptionManager();
    }

    @Override
    public InterpreterManager getInterpreterManager() {
        return interpreterManager;
    }

    @Override
    public DeploymentManager getDeploymentManager() {
        return deploymentManager;
    }

    public <T extends QueryManager> T getQueryManager(Class<T> queryManagerClass) throws UnsupportedQueryManagerException {
        QueryManager queryManager = queryManagers.get(queryManagerClass);
        if (queryManager == null) {
            throw new UnsupportedQueryManagerException();
        }
        return (T) queryManager;
    }

    @Override
    public ExceptionManager getExceptionManager() {
        return exceptionManager;
    }

    @Override
    public void preconditionFulfilled(PreconditionFulfilledEvent evt) {
        try {
            preconditionFulfilled(evt.getPrecondition(), evt.getMatch());
        } catch (PolicyHandlerException ex) {
            getExceptionManager().throwException(ex, this.getClass());
        }
    }

    @Override
    public void fulfilledPreconditionMatchesCreated(MatchesCreatedEvent evt) {
        try {
            fulfilledPreconditionMatchesCreated(evt.getPrecondition(), evt.getCreatedMatches(), evt.getOldMatches());
        } catch (PolicyHandlerException ex) {
            getExceptionManager().throwException(ex, this.getClass());
        }
    }

    @Override
    public void fulfilledPreconditionMatchesCancelled(MatchesCancelledEvent evt) {
        try {
            fulfilledPreconditionMatchesCancelled(evt.getPrecondition(), evt.getCancelledMatches(), evt.getActiveMatches());
        } catch (PolicyHandlerException ex) {
            getExceptionManager().throwException(ex, this.getClass());
        }
    }

    @Override
    public void preconditionUnfulfilled(PreconditionUnfulfilledEvent evt) {
        try {
            preconditionUnfulfilled(evt.getPrecondition(), evt.getCancelledMatch());
        } catch (PolicyHandlerException ex) {
            getExceptionManager().throwException(ex, this.getClass());
        }
    }

    public abstract void preconditionFulfilled(Precondition precondition, Match match) throws PolicyHandlerException;

    public abstract void preconditionUnfulfilled(Precondition precondition, Match cancelledMatch) throws PolicyHandlerException;

    public void fulfilledPreconditionMatchesCreated(Precondition precondition, List<Match> createdMatches, List<Match> oldMatches) throws PolicyHandlerException {
        // Default empty implementation for policy handlers not interested in this event.
    }

    public void fulfilledPreconditionMatchesCancelled(Precondition precondition, List<Match> cancelledMatches, List<Match> activeMatches) throws PolicyHandlerException {
        // Default empty implementation for policy handlers not interested in this event.
    }

    public File getConfigurationFile() throws PolicyHandlerInitializationException {
        return getConfigurationFile(configuration.getConfigurationLocation(), DEFAULT_CONFIGURATION_FILE_NAME_SUFFIX);
    }

    public File getConfigurationFile(File configurationDirectory) throws PolicyHandlerInitializationException {
        return getConfigurationFile(configurationDirectory, DEFAULT_CONFIGURATION_FILE_NAME_SUFFIX);
    }

    public File getConfigurationFile(File configurationDirectory, String configurationFileNameSuffix) throws PolicyHandlerInitializationException {
        if (!configurationDirectory.exists()) {
            throw new PolicyHandlerInitializationException("Configuration directory not found: " + configurationDirectory.getAbsolutePath());
        }
        if (!configurationDirectory.isDirectory()) {
            throw new PolicyHandlerInitializationException("Policy handler configuration location should be a directory.");
        }
        String configurationFileName = this.getClass().getSimpleName() + configurationFileNameSuffix;
        File configurationFile = new File(configurationDirectory, configurationFileName);
        if (!configurationFile.exists()) {
            throw new PolicyHandlerInitializationException("Configuration file not found: " + configurationFile.getAbsolutePath());
        }
        if (!configurationFile.canRead()) {
            throw new PolicyHandlerInitializationException("Unable to read configuration: " + configurationFile.getAbsolutePath());
        }
        return configurationFile;
    }
}
