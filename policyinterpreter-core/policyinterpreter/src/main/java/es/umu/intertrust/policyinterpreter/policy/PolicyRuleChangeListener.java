package es.umu.intertrust.policyinterpreter.policy;

import java.util.List;

/**
 *
 * @author Juanma
 */
public interface PolicyRuleChangeListener {

    public void policyRulesActivated(List<PolicyRule> policyRules);

    public void policyRulesDeactivated(List<PolicyRule> policyRules);

    public void activePolicyRulesUpdated(List<PolicyRule> policyRules);
}
