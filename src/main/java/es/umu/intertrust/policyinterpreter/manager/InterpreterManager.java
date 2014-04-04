package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentChangeListener;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcingException;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.deployment.HandlersDeploymentManager;
import es.umu.intertrust.policyinterpreter.dummy.DummyPolicyHandler;
import es.umu.intertrust.policyinterpreter.policy.PolicyChangeListener;
import es.umu.intertrust.policyinterpreter.policy.PolicyManager;
import es.umu.intertrust.policyinterpreter.policy.handler.PolicyHandler;
import es.umu.intertrust.policyinterpreter.policy.PolicyRule;
import es.umu.intertrust.policyinterpreter.policy.handler.Match;
import es.umu.intertrust.policyinterpreter.policy.handler.Precondition;
import es.umu.intertrust.policyinterpreter.query.QueryManager;
import java.util.ArrayList;
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
public class InterpreterManager implements PolicyChangeListener, DeploymentChangeListener {

    PolicyManager policyManager;
    DeploymentManager deploymentManager;
    Engine engine;
    Map<Precondition, PreconditionData> preconditionData;

    public InterpreterManager(PolicyManager policyManager, DeploymentManager deploymentManager) {
        this.policyManager = policyManager;
        this.deploymentManager = deploymentManager;
        this.engine = new Engine();
        this.preconditionData = new HashMap<Precondition, PreconditionData>();
        policyManager.addPolicyChangeListener(this);
        deploymentManager.addDeploymentChangeListener(this);
    }

    public void loadPolicyHandlers(Collection<QueryManager> queryManagers) {
        HandlersDeploymentManager handlersDeploymentManager = new HandlersDeploymentManager(deploymentManager);
        Map<Precondition, PreconditionData> data = new HashMap<Precondition, PreconditionData>();

        // TODO - Link with Plug-in mechanism
        PolicyHandler handler = new DummyPolicyHandler();

        handler.setManagers(queryManagers, handlersDeploymentManager);
        for (Precondition precondition : handler.getPreconditions()) {
            data.put(precondition, new PreconditionData(handler));
        }
        this.preconditionData = data;
        try {
            engine.initialize(data.keySet());
        } catch (EngineInitializationException ex) {
            Logger.getLogger(InterpreterManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public void policyRulesActivated(List<PolicyRule> policyRules) {
        for (PolicyRule policyRule : policyRules) {
            engine.policyRuleActivated(policyRule);
        }
        evaluateAndEnforce();
    }

    public void policyRulesDeactivated(List<PolicyRule> policyRules) {
        for (PolicyRule policyRule : policyRules) {
            engine.policyRuleDectivated(policyRule);
        }
        evaluateAndEnforce();
    }

    public void deploymentFeatureAdded(DeploymentFeature feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deploymentFeatureRemoved(DeploymentFeature feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deploymentFeatureUpdated(DeploymentFeature feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void evaluateAndEnforce() {
        engineEvaluation();
        if (!policyManager.hasMoreChanges() && deploymentManager.deploymentChanged()) {
            try {
                deploymentManager.enforce();
            } catch (DeploymentEnforcingException ex) {
                Logger.getLogger(InterpreterManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }

    private void engineEvaluation() {
        EngineMatchGatherer gatherer = new EngineMatchGatherer();
        engine.addPreconditionMatchingListener(gatherer);
        engine.evaluate();
        engine.removePreconditionMatchingListener(gatherer);

        Map<Precondition, List<EngineMatch>> createdMatches = getMatchesByPrecondition(gatherer.getCreatedMatches());
        Map<Precondition, List<EngineMatch>> cancelledMatches = getMatchesByPrecondition(gatherer.getCancelledMatches());

        updatePreconditionDataWithCreatedMatches(createdMatches);
        updatePreconditionDataWithCancelledMatches(cancelledMatches);
    }

    private Map<Precondition, List<EngineMatch>> getMatchesByPrecondition(List<EngineMatchEvent> events) {
        Map<Precondition, List<EngineMatch>> matches = new HashMap<Precondition, List<EngineMatch>>();
        for (EngineMatchEvent evt : events) {
            Precondition precondition = evt.getPrecondition();
            List<EngineMatch> preconditionMatches = matches.get(precondition);
            if (preconditionMatches == null) {
                preconditionMatches = new ArrayList<EngineMatch>();
                matches.put(precondition, preconditionMatches);
            }
            preconditionMatches.add(evt.getMatch());
        }
        return matches;
    }

    private void updatePreconditionDataWithCreatedMatches(Map<Precondition, List<EngineMatch>> matches) {
        for (Map.Entry<Precondition, List<EngineMatch>> preconditionMatches : matches.entrySet()) {
            Precondition precondition = preconditionMatches.getKey();
            PreconditionData data = preconditionData.get(precondition);
            if (data == null) {
                throw new IllegalStateException("Unhandled precondition matched: " + precondition);
            }
            List<Match> createdMatches = generateHandlerMatches(preconditionMatches.getValue());
            List<Match> oldMatches = generateHandlerMatches(data.getMatches());
            boolean alreadyFulfilled = data.isPreconditionFulfilled();
            data.addMatches(preconditionMatches.getValue());
            if (alreadyFulfilled) {
                data.getHandler().fulfilledPreconditionMatchesCreated(new DefaultMatchesCreatedEvent(precondition, createdMatches, oldMatches));
            } else {
                data.getHandler().preconditionFulfilled(new DefaultPreconditionFulfilledEvent(precondition, createdMatches));
            }
        }
    }

    private void updatePreconditionDataWithCancelledMatches(Map<Precondition, List<EngineMatch>> matches) {
        for (Map.Entry<Precondition, List<EngineMatch>> preconditionMatches : matches.entrySet()) {
            Precondition precondition = preconditionMatches.getKey();
            PreconditionData data = preconditionData.get(precondition);
            if (data == null) {
                throw new IllegalStateException("Unhandled precondition unmatched: " + precondition);
            }
            data.removeMatches(preconditionMatches.getValue());
            List<Match> cancelledMatches = generateHandlerMatches(preconditionMatches.getValue());
            List<Match> activeMatches = generateHandlerMatches(data.getMatches());
            if (data.isPreconditionFulfilled()) {
                data.getHandler().fulfilledPreconditionMatchesCancelled(new DefaultMatchesCancelledEvent(precondition, cancelledMatches, activeMatches));
            } else {
                data.getHandler().preconditionUnfulfilled(new DefaultPreconditionUnfulfilledEvent(precondition, cancelledMatches));
            }
        }
    }

    private List<Match> generateHandlerMatches(List<EngineMatch> matches) {
        List<Match> handlerMatches = new ArrayList<Match>();
        return handlerMatches;
    }
}
