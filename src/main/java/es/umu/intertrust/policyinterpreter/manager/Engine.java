package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.policy.PolicyRule;
import es.umu.intertrust.policyinterpreter.policy.handler.Precondition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

/**
 *
 * @author Juanma
 */
public class Engine {

    KieSession session;
    Map<String, Precondition> preconditionsByRuleName;
    Map<PolicyRule, FactHandle> factHandles;
    List<EngineMatchListener> listeners;

    public Engine() {
        this.session = null;
        this.preconditionsByRuleName = new HashMap<String, Precondition>();
        this.factHandles = new HashMap<PolicyRule, FactHandle>();
        this.listeners = new ArrayList<EngineMatchListener>();
    }

    public void initialize(Collection<Precondition> preconditions) throws EngineInitializationException {
        if (this.session != null) {
            throw new IllegalStateException("Engine already initialized.");
        }
        KieServices services = KieServices.Factory.get();
        KieFileSystem fileSystem = services.newKieFileSystem();
        generateRules(preconditions, fileSystem);
        KieBuilder builder = services.newKieBuilder(fileSystem);
        builder.buildAll();
        if (builder.getResults().hasMessages(Level.ERROR)) {
            throw new EngineInitializationException("Engine initialization errors:\n" + builder.getResults().toString());
        }
        KieContainer container = services.newKieContainer(services.getRepository().getDefaultReleaseId());
        this.session = container.newKieSession();
        this.session.addEventListener(new EngineRuleRuntimeEventListener());
    }

    private void generateRules(Collection<Precondition> preconditions, KieFileSystem fileSystem) throws EngineInitializationException {
        int ruleNamesIndex = 0;
        String drls = "package es.umu.intertrust.policyinterpreter.orbac";
        drls += " import es.umu.intertrust.policyinterpreter.manager.EngineMatch";
        for (Precondition precondition : preconditions) {
            String evaluationString = precondition.getEvaluationCondition();
            if ((evaluationString == null) || (evaluationString.isEmpty())) {
                throw new EngineInitializationException("Empty precondition found.");
            }
            String ruleName = String.valueOf(ruleNamesIndex++);
            drls += " rule \"" + ruleName + "\" when "
                    + precondition.getEvaluationCondition()
                    + " then"
                    + " insertLogical(new EngineMatch(drools.getMatch()));"
                    + " end";
            preconditionsByRuleName.put(ruleName, precondition);
        }
        fileSystem.write("src/main/resources/preconditions.drl", drls);
    }

    public void policyRuleActivated(PolicyRule policyRule) {
        FactHandle handle = session.insert(policyRule);
        factHandles.put(policyRule, handle);
    }

    public void policyRuleDectivated(PolicyRule policyRule) {
        FactHandle handle = factHandles.get(policyRule);
        if (handle != null) {
            session.delete(handle);
        }
    }

    public void deploymentFeatureAdded(DeploymentFeature deploymentFeature) {

    }

    public void deploymentFeatureRemoved(DeploymentFeature deploymentFeature) {

    }

    public void evaluate() {
        session.fireAllRules();
    }

    public void addPreconditionMatchingListener(EngineMatchListener listener) {
        listeners.add(listener);
    }

    public void removePreconditionMatchingListener(EngineMatchListener listener) {
        listeners.remove(listener);
    }

    public void fireMatchCreated(EngineMatchEvent evt) {
        for (EngineMatchListener listener : listeners) {
            listener.matchCreated(evt);
        }
    }

    public void fireMatchCancelled(EngineMatchEvent evt) {
        for (EngineMatchListener listener : listeners) {
            listener.matchCancelled(evt);
        }
    }

    class EngineRuleRuntimeEventListener implements RuleRuntimeEventListener {

        public void objectInserted(ObjectInsertedEvent event) {
            Object insertedObject = event.getObject();
            if (insertedObject instanceof EngineMatch) {
                EngineMatch match = (EngineMatch) insertedObject;
                Precondition precondition = preconditionsByRuleName.get(match.getRuleName());
                if (precondition == null) {
                    throw new EngineRuntimeException("Unknown rule matched: " + match.getRuleName());
                }
                fireMatchCreated(new EngineMatchEvent(precondition, match));
            }
        }

        public void objectUpdated(ObjectUpdatedEvent event) {
            throw new EngineRuntimeException("Unexpected object update: " + event.getObject());
        }

        public void objectDeleted(ObjectDeletedEvent event) {
            Object deletedObject = event.getOldObject();
            if (deletedObject instanceof EngineMatch) {
                EngineMatch match = (EngineMatch) deletedObject;
                Precondition precondition = preconditionsByRuleName.get(match.getRuleName());
                if (precondition == null) {
                    throw new EngineRuntimeException("Unknown rule unmatched: " + match.getRuleName());
                }
                fireMatchCancelled(new EngineMatchEvent(precondition, match));
            }
        }
    }

}
