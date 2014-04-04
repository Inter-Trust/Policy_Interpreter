package es.umu.intertrust.policyinterpreter.policy;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class PolicyManager implements PolicyChangeListener {

    List<PolicyRule> activePolicyRules;
    List<PolicyChangeListener> listeners;

    public PolicyManager() {
        this.activePolicyRules = new ArrayList<PolicyRule>();
        this.listeners = new ArrayList<PolicyChangeListener>();
    }

    public void policyRulesActivated(List<PolicyRule> policyRules) {
        this.activePolicyRules.addAll(policyRules);
        firePolicyRulesActivated(policyRules);
    }

    public void policyRulesDeactivated(List<PolicyRule> policyRules) {
        this.activePolicyRules.removeAll(policyRules);
        firePolicyRulesDeactivated(policyRules);
    }

    public boolean hasMoreChanges() {
        // TODO - Use gatherer to know whether more notification changes have been received
        return false;
    }

    public void addPolicyChangeListener(PolicyChangeListener listener) {
        listeners.add(listener);
    }

    public void removePolicyChangeListener(PolicyChangeListener listener) {
        listeners.remove(listener);
    }

    protected void firePolicyRulesActivated(List<PolicyRule> policyRules) {
        for (PolicyChangeListener listener : listeners) {
            listener.policyRulesActivated(policyRules);
        }
    }

    protected void firePolicyRulesDeactivated(List<PolicyRule> policyRules) {
        for (PolicyChangeListener listener : listeners) {
            listener.policyRulesDeactivated(policyRules);
        }
    }
}
