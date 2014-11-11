package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.Configuration;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.Target;
import es.umu.intertrust.policyinterpreter.policy.PolicyRule;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.util.collections.BidirectionalHashMap;
import es.umu.intertrust.policyinterpreter.util.collections.BidirectionalMap;
import es.umu.intertrust.policyinterpreter.util.collections.ListHashMap;
import es.umu.intertrust.policyinterpreter.util.collections.ListMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;

import org.kie.api.builder.ReleaseId;
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

    public static final Logger logger = Logger.getLogger(InterpreterManager.class.getName());

    static String RELEASE_GROUP_ID = "es.umu.intertrust";
    static String RELEASE_ARTIFACT_ID = "PolicyInterpreter";

    String imports;
    KieServices services;
    KieFileSystem fileSystem;
    KieContainer container;
    KieSession session;
    long versionNumber;
    boolean knowledgeBaseChanged;
    BidirectionalMap<String, Precondition> preconditionRuleNames;
    Map<PolicyRule, FactHandle> policyFactHandles;
    Map<DeploymentFeature, FactHandle> deploymentFactHandles;
    Map<Target, FactHandle> targetFactHandles;
    ListMap<Target, AssignedTarget> assignedTargets;
    Map<AssignedTarget, FactHandle> assignedTargetFactHandles;
    List<EngineMatchListener> listeners;

    public Engine() {
        this.session = null;
        this.preconditionRuleNames = new BidirectionalHashMap<String, Precondition>();
        this.policyFactHandles = new HashMap<PolicyRule, FactHandle>();
        this.deploymentFactHandles = new HashMap<DeploymentFeature, FactHandle>();
        this.targetFactHandles = new HashMap<Target, FactHandle>();
        this.assignedTargets = new ListHashMap<Target, AssignedTarget>();
        this.assignedTargetFactHandles = new HashMap<AssignedTarget, FactHandle>();
        this.knowledgeBaseChanged = false;
        this.listeners = new ArrayList<EngineMatchListener>();
    }

    public void initialize(Collection<Precondition> preconditions) throws EngineException {
        generateImports();
        services = KieServices.Factory.get();
        fileSystem = services.newKieFileSystem();
        versionNumber = 1;
        ReleaseId release = generateRelease(preconditions, null);
        container = services.newKieContainer(release);
        session = container.newKieSession();
        session.addEventListener(new EngineRuleRuntimeEventListener());
    }

    public void addPrecondition(Precondition precondition) throws EngineException {
        addPreconditions(Arrays.asList(new Precondition[]{precondition}));
    }

    public void addPreconditions(Collection<Precondition> preconditions) throws EngineException {
        logger.log(Level.FINER, "Adding preconditions: {0}", preconditions);
        ReleaseId release = generateRelease(preconditions, null);
        container.updateToVersion(release);
        knowledgeBaseChanged = true;
    }

    public void removePrecondition(Precondition precondition) throws EngineException {
        removePreconditions(Arrays.asList(new Precondition[]{precondition}));
    }

    public void removePreconditions(Collection<Precondition> preconditions) throws EngineException {
        logger.log(Level.FINER, "Removing preconditions: {0}", preconditions);
        ReleaseId release = generateRelease(null, preconditions);
        container.updateToVersion(release);
        knowledgeBaseChanged = true;
    }

    private ReleaseId generateRelease(Collection<Precondition> preconditionsToAdd, Collection<Precondition> preconditionsToRemove) throws EngineException {
        if (services == null) {
            throw new IllegalStateException("Engine not initialized.");
        }
        ReleaseId release = services.newReleaseId(RELEASE_GROUP_ID, RELEASE_ARTIFACT_ID, String.valueOf(versionNumber++));
        if (preconditionsToAdd != null) {
            addPreconditionsRules(preconditionsToAdd);
        }
        if (preconditionsToRemove != null) {
            deletePreconditionRules(preconditionsToRemove);
        }
        fileSystem.generateAndWritePomXML(release);
        KieBuilder builder = services.newKieBuilder(fileSystem);
        builder.buildAll();
        if (builder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            // TODO - Check if the error is due to wrong evaluation string specifications and launch a specific exception
            throw new EngineException("Engine errors:\n" + builder.getResults().toString());
        }
        return release;
    }

    private void addPreconditionsRules(Collection<Precondition> preconditions) throws EngineException {
        for (Precondition precondition : preconditions) {
            String ruleName = UUID.randomUUID().toString();
            preconditionRuleNames.put(ruleName, precondition);
            fileSystem.write("src/main/resources/" + ruleName + ".drl", generateDrlFile(ruleName, precondition));
        }
    }

    private void deletePreconditionRules(Collection<Precondition> preconditions) throws EngineException {
        for (Precondition precondition : preconditions) {
            String ruleName = preconditionRuleNames.getByValue(precondition);
            preconditionRuleNames.removeByValue(precondition);
            fileSystem.delete("src/main/resources/" + ruleName + ".drl");
        }
    }

    private String generateDrlFile(String ruleName, Precondition precondition) throws EngineException {
        return this.imports + " " + generateRule(ruleName, precondition);
    }

    private void generateImports() {
        this.imports = "import es.umu.intertrust.policyinterpreter.manager.EngineMatch";
        this.imports += " import es.umu.intertrust.policyinterpreter.manager.AssignedTarget";
        for (String importClass : Configuration.getInstance().getPreconditionAvailableClasses()) {
            this.imports += " import " + importClass;
        }
    }

    private String generateRule(String ruleName, Precondition precondition) throws EngineException {
        String evaluationString = precondition.getEvaluationCondition();
        if ((evaluationString == null) || (evaluationString.isEmpty())) {
            // TODO - Throw a dedicated exception for errors comming from PolicyHandlers developers
            throw new EngineException("Empty precondition found.");
        }
        String rule = "rule \"" + ruleName + "\" when "
                + precondition.getEvaluationCondition()
                + " then"
                + " insertLogical(new EngineMatch(drools.getMatch()));"
                + " end";
        return rule;
    }

    public void insertPolicyRule(PolicyRule policyRule) {
        logger.log(Level.FINER, "Inserting policy rule: {0}", policyRule);
        FactHandle handle = session.insert(policyRule);
        policyFactHandles.put(policyRule, handle);
        knowledgeBaseChanged = true;
    }

    public void deletePolicyRule(PolicyRule policyRule) {
        logger.log(Level.FINER, "Deleting policy rule: {0}", policyRule);
        FactHandle handle = policyFactHandles.get(policyRule);
        if (handle != null) {
            session.delete(handle);
            policyFactHandles.remove(policyRule);
            knowledgeBaseChanged = true;
        }
    }

    public void updatePolicyRule(PolicyRule policyRule) {
        logger.log(Level.FINER, "Updating policy rule: {0}", policyRule);
        FactHandle handle = policyFactHandles.get(policyRule);
        if (handle != null) {
            session.update(handle, policyRule);
            knowledgeBaseChanged = true;
        } else {
            throw new IllegalStateException("Updating non-existent policy rule: " + policyRule);
        }
    }

    public void addDeploymentFeature(DeploymentFeature deploymentFeature) {
        logger.log(Level.FINER, "Adding deployment feature: {0}", deploymentFeature);
        FactHandle handle = session.insert(deploymentFeature);
        deploymentFactHandles.put(deploymentFeature, handle);
        knowledgeBaseChanged = true;
    }

    public void deleteDeploymentFeature(DeploymentFeature deploymentFeature) {
        logger.log(Level.FINER, "Deleting deployment feature: {0}", deploymentFeature);
        FactHandle handle = deploymentFactHandles.get(deploymentFeature);
        if (handle != null) {
            session.delete(handle);
            deploymentFactHandles.remove(deploymentFeature);
            knowledgeBaseChanged = true;
        }
    }

    public void updateDeploymentFeature(DeploymentFeature deploymentFeature) {
        logger.log(Level.FINER, "Updating deployment feature: {0}", deploymentFeature);
        FactHandle handle = deploymentFactHandles.get(deploymentFeature);
        if (handle != null) {
            session.update(handle, deploymentFeature);
            knowledgeBaseChanged = true;
        } else {
            throw new IllegalStateException("Updating non-existent deployment feature: " + deploymentFeature);
        }
    }

    public void insertTarget(DeploymentFeature deploymentFeature, Target target) {
        logger.log(Level.FINER, "Inserting target: {0}", target);
        if (!targetFactHandles.containsKey(target)) {
            FactHandle handle = session.insert(target);
            targetFactHandles.put(target, handle);
        }
        AssignedTarget assignedTarget = new AssignedTarget(deploymentFeature, target);
        assignedTargets.addToList(target, assignedTarget);
        FactHandle handle = session.insert(assignedTarget);
        assignedTargetFactHandles.put(assignedTarget, handle);
        knowledgeBaseChanged = true;
    }

    public void deleteTarget(DeploymentFeature deploymentFeature, Target target) {
        logger.log(Level.FINER, "Deleting target: {0}", deploymentFeature);
        AssignedTarget assignedTarget = new AssignedTarget(deploymentFeature, target);
        FactHandle handle = assignedTargetFactHandles.get(assignedTarget);
        if (handle != null) {
            session.delete(handle);
            assignedTargetFactHandles.remove(assignedTarget);
            knowledgeBaseChanged = true;
        }
        List<AssignedTarget> targetAssignedTargets = assignedTargets.getList(target);
        if (targetAssignedTargets != null) {
            targetAssignedTargets.remove(assignedTarget);
            if (targetAssignedTargets.isEmpty()) {
                handle = targetFactHandles.get(target);
                if (handle != null) {
                    session.delete(handle);
                    targetFactHandles.remove(target);
                    knowledgeBaseChanged = true;
                }
            }
        }
    }

    public void evaluate() {
        logger.log(Level.FINER, "Performing engine evaluation.");
        session.fireAllRules();
        knowledgeBaseChanged = false;
    }

    public boolean knowledgeBaseChanged() {
        return knowledgeBaseChanged;
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
            logger.log(Level.FINEST, "Engine object inserted: {0}", insertedObject);
            if (insertedObject instanceof EngineMatch) {
                EngineMatch match = (EngineMatch) insertedObject;
                Precondition precondition = preconditionRuleNames.get(match.getRuleName());
                if (precondition == null) {
                    throw new EngineRuntimeException("Unknown rule matched: " + match.getRuleName());
                }
                fireMatchCreated(new EngineMatchEvent(precondition, match));
            }
        }

        public void objectUpdated(ObjectUpdatedEvent event) {
            logger.log(Level.FINEST, "Engine object updated: {0}", event.getObject());
            // TODO - Object update in Policy Engine
//            throw new EngineRuntimeException("Unexpected object update: " + event.getObject());
        }

        public void objectDeleted(ObjectDeletedEvent event) {
            Object deletedObject = event.getOldObject();
            logger.log(Level.FINEST, "Engine object deleted: {0}", deletedObject);
            if (deletedObject instanceof EngineMatch) {
                EngineMatch match = (EngineMatch) deletedObject;
                Precondition precondition = preconditionRuleNames.get(match.getRuleName());
                if (precondition == null) {
                    throw new EngineRuntimeException("Unknown rule unmatched: " + match.getRuleName());
                }
                fireMatchCancelled(new EngineMatchEvent(precondition, match));
            }
        }
    }

}
