package es.umu.intertrust.policyinterpreter.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Juanma
 */
public abstract class AbstractPolicyNotificationHandler implements PolicyNotificationHandler {

    List<PolicyChangeListener> listeners;

    public AbstractPolicyNotificationHandler() {
        this.listeners = new ArrayList<PolicyChangeListener>();
    }

    public void addPolicyChangeListener(PolicyChangeListener listener) {
        listeners.add(listener);
    }

    public void removePolicyChangeListener(PolicyChangeListener listener) {
        listeners.remove(listener);
    }

    protected void firePolicyRuleActivated(PolicyRule policyRule) {
        firePolicyRulesActivated(Arrays.asList(new PolicyRule[]{policyRule}));
    }

    protected void firePolicyRuleDeactivated(PolicyRule policyRule) {
        firePolicyRulesDeactivated(Arrays.asList(new PolicyRule[]{policyRule}));
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
