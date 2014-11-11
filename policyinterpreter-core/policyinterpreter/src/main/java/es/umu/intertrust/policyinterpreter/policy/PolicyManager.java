package es.umu.intertrust.policyinterpreter.policy;

import es.umu.intertrust.policyinterpreter.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class PolicyManager implements PolicyChangeListener {

    public static final Logger logger = Logger.getLogger(PolicyManager.class.getName());

    private enum ChangeType {

        ACTIVATED, DEACTIVATED, UPDATED;
    }
    Properties properties;
    List<PolicyRule> activePolicyRules;
    boolean hasChanges;
    PolicyChangesGatherer gatherer;
    PolicyNotificationHandler notificationHandler;
    boolean warnUnrelatedDeactivations;
    List<PolicyRuleChangeListener> listeners;

    public PolicyManager(Properties properties) {
        this.properties = properties;
        this.activePolicyRules = new ArrayList<PolicyRule>();
        this.hasChanges = false;
        this.warnUnrelatedDeactivations = ConfigurationProperties.POLICY_MANAGER_WARN_UNRELATED_RULE_DEACTIVATIONS.getBoolean(properties);
        this.listeners = new ArrayList<PolicyRuleChangeListener>();
    }

    public Properties getPolicyProperties() {
        return properties;
    }

    public PolicyNotificationHandler getNotificationHandler() {
        if (notificationHandler == null) {
            throw new IllegalStateException("Notification handler not set.");
        }
        return notificationHandler;
    }

    public void setNotificationHandler(PolicyNotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
        gatherer = new PolicyChangesGatherer(notificationHandler);
        gatherer.addPolicyChangeListener(this);
    }

    public void startListeningToChanges() throws PolicyNotificationHandlerException {
        logger.log(Level.INFO, "Starting policy notification handler...");
        notificationHandler.start();
    }

    public void stopListeningToChanges() throws PolicyNotificationHandlerException {
        notificationHandler.stop();
    }

    public void policyStatusChanged(List<PolicyRuleChange> policyRuleChanges) {
        logger.log(Level.FINEST, "Processing {0} policy rule changes.", policyRuleChanges.size());
        logger.log(Level.FINEST, "Current active policy rules: {0}", activePolicyRules);
        List<PolicyRule> changedRules = new ArrayList<PolicyRule>();
        if (!policyRuleChanges.isEmpty()) {
            hasChanges = true;
            PolicyRuleChange change = policyRuleChanges.get(0);
            ChangeType changeType = determineChangeType(change);
            for (int i = 0; i < policyRuleChanges.size(); i++) {
                PolicyRule changedRule = change.getPolicyRule();
                changedRules.add(changedRule);
                updateActivePolicyRules(changeType, changedRule);
                PolicyRuleChange nextChange = null;
                ChangeType nextChangeType = null;
                if (i + 1 < policyRuleChanges.size()) {
                    nextChange = policyRuleChanges.get(i + 1);
                    nextChangeType = determineChangeType(nextChange);
                } else {
                    hasChanges = false;
                }
                if (changeType != nextChangeType) {
                    notifyPolicyRuleChanges(changeType, changedRules);
                    changedRules.clear();
                    changeType = nextChangeType;
                }
                change = nextChange;
            }
        }
        logger.log(Level.FINEST, "Updated active policy rules: {0}", activePolicyRules);
    }

    private ChangeType determineChangeType(PolicyRuleChange policyRuleChange) {
        if (policyRuleChange.isPolicyRuleActive()) {
            if (activePolicyRules.contains(policyRuleChange.getPolicyRule())) {
                return ChangeType.UPDATED;
            } else {
                return ChangeType.ACTIVATED;
            }
        } else {
            if (warnUnrelatedDeactivations && !activePolicyRules.contains(policyRuleChange.getPolicyRule())) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Received deactivation notification for a rule not activated.");
            }
            return ChangeType.DEACTIVATED;
        }
    }

    private void updateActivePolicyRules(ChangeType changeType, PolicyRule changedPolicyRule) {
        switch (changeType) {
            case ACTIVATED:
                activePolicyRules.add(changedPolicyRule);
                logger.log(Level.FINEST, "Added policy rule: {0}", changedPolicyRule);
                break;
            case DEACTIVATED:
                activePolicyRules.remove(changedPolicyRule);
                logger.log(Level.FINEST, "Removed policy rule: {0}", changedPolicyRule);
                break;
            case UPDATED:
                // Removing and adding should be done in case equals is overriden by the policy rule
                activePolicyRules.remove(changedPolicyRule);
                activePolicyRules.add(changedPolicyRule);
                logger.log(Level.FINEST, "Updated policy rule: {0}", changedPolicyRule);
                break;
        }
    }

    private void notifyPolicyRuleChanges(ChangeType changeType, List<PolicyRule> changedPolicyRules) {
        switch (changeType) {
            case ACTIVATED:
                logger.log(Level.FINEST, "Notifying ACTIVATED rules: {0}", changedPolicyRules);
                firePolicyRulesActivated(changedPolicyRules);
                break;
            case DEACTIVATED:
                logger.log(Level.FINEST, "Notifying DEACTIVATED rules: {0}", changedPolicyRules);
                firePolicyRulesDeactivated(changedPolicyRules);
                break;
            case UPDATED:
                logger.log(Level.FINEST, "Notifying UPDATED rules: {0}", changedPolicyRules);
                fireActivePolicyRulesUpdated(changedPolicyRules);
                break;
        }
    }

    public boolean hasMoreChanges() {
        return hasChanges || gatherer.hasPolicyRuleChanges();
    }

    public void addPolicyRuleChangeListener(PolicyRuleChangeListener listener) {
        listeners.add(listener);
    }

    public void removePolicyRuleChangeListener(PolicyRuleChangeListener listener) {
        listeners.remove(listener);
    }

    protected void firePolicyRulesActivated(List<PolicyRule> policyRules) {
        for (PolicyRuleChangeListener listener : listeners) {
            listener.policyRulesActivated(policyRules);
        }
    }

    protected void firePolicyRulesDeactivated(List<PolicyRule> policyRules) {
        for (PolicyRuleChangeListener listener : listeners) {
            listener.policyRulesDeactivated(policyRules);
        }
    }

    protected void fireActivePolicyRulesUpdated(List<PolicyRule> policyRules) {
        for (PolicyRuleChangeListener listener : listeners) {
            listener.activePolicyRulesUpdated(policyRules);
        }
    }
}
