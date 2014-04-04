package es.umu.intertrust.policyinterpreter.policy;

import java.util.List;

/**
 *
 * @author Juanma
 */
public interface PolicyChangeListener {

    public void policyRulesActivated(List<PolicyRule> policyRules);

    public void policyRulesDeactivated(List<PolicyRule> policyRules);
}
