package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.Configuration;
import es.umu.intertrust.policyinterpreter.util.modules.ModuleLoadingExceptions;
import es.umu.intertrust.policyinterpreter.util.modules.ModuleLoader;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentChangeListener;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcingException;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.deployment.HandlersDeploymentManager;
import es.umu.intertrust.policyinterpreter.deployment.Target;
import es.umu.intertrust.policyinterpreter.policy.PolicyManager;
import es.umu.intertrust.policyinterpreter.policy.PolicyNotificationHandlerException;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandler;
import es.umu.intertrust.policyinterpreter.policy.PolicyRule;
import es.umu.intertrust.policyinterpreter.policy.PolicyRuleChangeListener;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.policyhandlers.QueryManager;
import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionManagementException;
import es.umu.intertrust.policyinterpreter.util.collections.ListHashMap;
import es.umu.intertrust.policyinterpreter.util.collections.ListMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class InterpreterManager implements PolicyRuleChangeListener, DeploymentChangeListener {

    public static final Logger logger = Logger.getLogger(InterpreterManager.class.getName());

    PolicyManager policyManager;
    DeploymentManager deploymentManager;
    Engine engine;
    Map<Precondition, PreconditionData> preconditionData;

    public InterpreterManager() {
        this.engine = new Engine();
        this.preconditionData = new HashMap<Precondition, PreconditionData>();
    }

    public void initialize(PolicyManager policyManager, DeploymentManager deploymentManager, Collection<QueryManager> queryManagers) throws InitializationException {
        this.policyManager = policyManager;
        this.deploymentManager = deploymentManager;
        policyManager.addPolicyRuleChangeListener(this);
        deploymentManager.addDeploymentChangeListener(this);
        loadPolicyHandlers(queryManagers);
    }

    private void loadPolicyHandlers(Collection<QueryManager> queryManagers) throws InitializationException {
        try {
            logger.log(Level.INFO, "Loading policy handlers...");
            HandlersDeploymentManager handlersDeploymentManager = new HandlersDeploymentManager(deploymentManager);
            ModuleLoader loader = new ModuleLoader(PolicyHandler.class);
            loader.loadModules(Configuration.getInstance().getPolicyHandlersDirectory());
            for (PolicyHandler policyHandler : loader.getLoadedInstances(PolicyHandler.class)) {
                logger.log(Level.INFO, "Loading {0}.", policyHandler.getClass().getSimpleName());
                HandlerInterpreterManager handlerInterpreterManager = new HandlerInterpreterManager(this, policyHandler);
                HandlersExceptionManager handlersExceptionManager = new HandlersExceptionManager();
                HandlerConfiguration handlerConfiguration = new HandlerConfiguration(handlerInterpreterManager, handlersDeploymentManager, queryManagers, handlersExceptionManager);
                policyHandler.initialize(handlerConfiguration);
                for (Precondition precondition : policyHandler.getPreconditions()) {
                    preconditionData.put(precondition, new PreconditionData(policyHandler));
                }
            }
            logger.log(Level.INFO, "Policy handlers loaded.");
            try {
                logger.log(Level.INFO, "Initializing engine...");
                engine.initialize(preconditionData.keySet());
                logger.log(Level.INFO, "Engine initialized.");
            } catch (EngineException ex) {
                throw new InitializationException(ex.getMessage(), ex);
            }
        } catch (PolicyHandlerException ex) {
            throw new InitializationException(ex.getMessage(), ex);
        } catch (ModuleLoadingExceptions ex) {
            throw new InitializationException(ex.getMessage(), ex);
        }
    }

    public void addPrecondition(Precondition precondition, PolicyHandler handler) throws PreconditionManagementException {
        logger.log(Level.FINEST, "Adding precondition: {0}", precondition);
        try {
            engine.addPrecondition(precondition);
            preconditionData.put(precondition, new PreconditionData(handler));
        } catch (EngineException ex) {
            throw new PreconditionManagementException("Unable to add precondition : " + ex.getMessage(), ex);
        }
    }

    public void addPreconditions(Collection<Precondition> preconditions, PolicyHandler handler) throws PreconditionManagementException {
        logger.log(Level.FINEST, "Adding preconditions: {0}", preconditions);
        try {
            engine.addPreconditions(preconditions);
            for (Precondition precondition : preconditions) {
                preconditionData.put(precondition, new PreconditionData(handler));
            }
        } catch (EngineException ex) {
            throw new PreconditionManagementException("Unable to add preconditions : " + ex.getMessage(), ex);
        }
    }

    public void removePrecondition(Precondition precondition, PolicyHandler handler) throws PreconditionManagementException {
        logger.log(Level.FINEST, "Removing precondition: {0}", precondition);
        try {
            engine.removePrecondition(precondition);
            PreconditionData data = preconditionData.get(precondition);
            if ((data != null) && data.getHandler().equals(handler)) {
                preconditionData.remove(precondition);
            }
        } catch (EngineException ex) {
            throw new PreconditionManagementException("Unable to remove precondition : " + ex.getMessage(), ex);
        }
    }

    public void removePreconditions(Collection<Precondition> preconditions, PolicyHandler handler) throws PreconditionManagementException {
        logger.log(Level.FINEST, "Removing preconditions: {0}", preconditions);
        try {
            engine.addPreconditions(preconditions);
            for (Precondition precondition : preconditions) {
                PreconditionData data = preconditionData.get(precondition);
                if ((data != null) && data.getHandler().equals(handler)) {
                    preconditionData.remove(precondition);
                }
            }
        } catch (EngineException ex) {
            throw new PreconditionManagementException("Unable to remove preconditions : " + ex.getMessage(), ex);
        }
    }

    public void start() throws InterpreterManagerException {
        try {
            policyManager.startListeningToChanges();
        } catch (PolicyNotificationHandlerException ex) {
            throw new InterpreterManagerException(ex.getMessage(), ex);
        }
    }

    public void stop() throws InterpreterManagerException {
        try {
            policyManager.stopListeningToChanges();
        } catch (PolicyNotificationHandlerException ex) {
            throw new InterpreterManagerException(ex.getMessage(), ex);
        }
    }

    public void policyRulesActivated(List<PolicyRule> policyRules) {
        for (PolicyRule policyRule : policyRules) {
            engine.insertPolicyRule(policyRule);
        }
        try {
            evaluateAndEnforce();
        } catch (DeploymentEnforcingException ex) {
            logger.log(Level.SEVERE, "Error processing policy rule activation: " + ex.getMessage(), ex);
        }
    }

    public void policyRulesDeactivated(List<PolicyRule> policyRules) {
        for (PolicyRule policyRule : policyRules) {
            engine.deletePolicyRule(policyRule);
        }
        try {
            evaluateAndEnforce();
        } catch (DeploymentEnforcingException ex) {
            logger.log(Level.SEVERE, "Error processing policy rule deactivation: " + ex.getMessage(), ex);
        }
    }

    public void activePolicyRulesUpdated(List<PolicyRule> policyRules) {
// TODO: Active policy rules update
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Received {0} policy rules already activated. Ignoring...", policyRules.size());
//        throw new UnsupportedOperationException("Active policy rule update not supported.");
//        for (PolicyRule policyRule : policyRules) {
//            engine.updatePolicyRule(policyRule);
//        }
        try {
            evaluateAndEnforce();
        } catch (DeploymentEnforcingException ex) {
            logger.log(Level.SEVERE, "Error processing policy rule update: " + ex.getMessage(), ex);
        }
    }

    public void deploymentFeatureAdded(DeploymentFeature feature) {
        engine.addDeploymentFeature(feature);
    }

    public void deploymentFeatureRemoved(DeploymentFeature feature) {
        engine.deleteDeploymentFeature(feature);
    }

    public void deploymentFeatureUpdated(DeploymentFeature feature) {
        // TODO: updating a feature will cause matching precondition matches to be cancelled and activated again with the new feature, even if the checked fields are unchanged
        engine.updateDeploymentFeature(feature);
    }

    public void deploymentFeatureAddedToTarget(DeploymentFeature feature, Target target) {
        engine.insertTarget(feature, target);
    }

    public void deploymentFeatureRemovedFromTarget(DeploymentFeature feature, Target target) {
        engine.deleteTarget(feature, target);
    }

    private void evaluateAndEnforce() throws DeploymentEnforcingException {
        while (engine.knowledgeBaseChanged()) {
            engineEvaluation();
        }
        if (!policyManager.hasMoreChanges() && deploymentManager.deploymentChanged()) {
            deploymentManager.enforce();
        }
    }

    private void engineEvaluation() {
        logger.log(Level.FINEST, "Performing engine evaluation.");
        EngineMatchGatherer gatherer = new EngineMatchGatherer();
        engine.addPreconditionMatchingListener(gatherer);
        engine.evaluate();
        engine.removePreconditionMatchingListener(gatherer);

        ListMap<Precondition, EngineMatch> createdMatches = getMatchesByPrecondition(gatherer.getCreatedMatches());
        ListMap<Precondition, EngineMatch> cancelledMatches = getMatchesByPrecondition(gatherer.getCancelledMatches());

        updatePreconditionDataWithCreatedMatches(createdMatches);
        updatePreconditionDataWithCancelledMatches(cancelledMatches);
    }

    private ListMap<Precondition, EngineMatch> getMatchesByPrecondition(List<EngineMatchEvent> events) {
        ListMap<Precondition, EngineMatch> matches = new ListHashMap<Precondition, EngineMatch>();
        for (EngineMatchEvent evt : events) {
            matches.addToList(evt.getPrecondition(), evt.getMatch());
        }
        return matches;
    }

    private void updatePreconditionDataWithCreatedMatches(ListMap<Precondition, EngineMatch> matches) {
        logger.log(Level.FINEST, "Updating preconditions with created matches: {0}", matches);
        for (Map.Entry<Precondition, List<EngineMatch>> preconditionMatches : matches.entrySet()) {
            Precondition precondition = preconditionMatches.getKey();
            PreconditionData data = preconditionData.get(precondition);
            if (data == null) {
                throw new IllegalStateException("Unhandled precondition matched: " + precondition);
            }
            List<EngineMatch> createdMatches = preconditionMatches.getValue();
            if (!data.isPreconditionFulfilled() && !createdMatches.isEmpty()) {
                EngineMatch createdMatch = createdMatches.get(0);
                try {
                    logger.log(Level.FINEST, "Notifying policy handler with precondition fulfilled: {0}", createdMatch);
                    data.getHandler().preconditionFulfilled(new DefaultPreconditionFulfilledEvent(precondition, generateHandlerMatch(createdMatch)));
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "Policy handler error: " + t.getClass().getSimpleName() + ": " + t.getMessage(), t);
                }
                data.addMatch(createdMatch);
                createdMatches.remove(0);
            }
            if (!createdMatches.isEmpty()) {
                try {
                    logger.log(Level.FINEST, "Notifying policy handler with matches created: {0}", createdMatches);
                    data.getHandler().fulfilledPreconditionMatchesCreated(new DefaultMatchesCreatedEvent(precondition, generateHandlerMatches(createdMatches), generateHandlerMatches(data.getMatches())));
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "Policy handler error: " + t.getClass().getSimpleName() + ": " + t.getMessage(), t);
                }
                data.addMatches(createdMatches);
            }
        }
    }

    private void updatePreconditionDataWithCancelledMatches(ListMap<Precondition, EngineMatch> matches) {
        logger.log(Level.FINEST, "Updating preconditions with cancelled matches: {0}", matches);
        for (Map.Entry<Precondition, List<EngineMatch>> preconditionMatches : matches.entrySet()) {
            Precondition precondition = preconditionMatches.getKey();
            PreconditionData data = preconditionData.get(precondition);
            if (data == null) {
                throw new IllegalStateException("Unhandled precondition unmatched: " + precondition);
            }
            List<EngineMatch> cancelledMatches = preconditionMatches.getValue();
            data.removeMatches(cancelledMatches);
            if (data.isPreconditionFulfilled()) {
                try {
                    logger.log(Level.FINEST, "Notifying policy handler with matches cancelled: {0}", cancelledMatches);
                    data.getHandler().fulfilledPreconditionMatchesCancelled(new DefaultMatchesCancelledEvent(precondition, generateHandlerMatches(cancelledMatches), generateHandlerMatches(data.getMatches())));
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "Policy handler error: " + t.getClass().getSimpleName() + ": " + t.getMessage(), t);
                }
            } else {
                Match lastMatch = generateHandlerMatch(cancelledMatches.get(cancelledMatches.size() - 1));
                cancelledMatches.remove(cancelledMatches.size() - 1);
                try {
                    if (!cancelledMatches.isEmpty()) {
                        logger.log(Level.FINEST, "Notifying policy handler with matches cancelled: {0}", cancelledMatches);
                        data.getHandler().fulfilledPreconditionMatchesCancelled(new DefaultMatchesCancelledEvent(precondition, generateHandlerMatches(cancelledMatches), Arrays.asList(new Match[]{lastMatch})));
                    }
                    logger.log(Level.FINEST, "Notifying policy handler with precondition unfulfilled: {0}", lastMatch);
                    data.getHandler().preconditionUnfulfilled(new DefaultPreconditionUnfulfilledEvent(precondition, lastMatch));
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "Policy handler error: " + t.getClass().getSimpleName() + ": " + t.getMessage(), t);
                }
            }
        }
    }

    private List<Match> generateHandlerMatches(List<EngineMatch> matches) {
        List<Match> handlerMatches = new ArrayList<Match>();
        for (EngineMatch match : matches) {
            handlerMatches.add(generateHandlerMatch(match));
        }
        return handlerMatches;
    }

    private Match generateHandlerMatch(EngineMatch match) {
        return new Match(match.getDeclaredMatchingValues(), match.getMatchingValues());
    }
}
