package es.umu.intertrust.policyinterpreter.policy;

/**
 *
 * @author Juanma
 */
public class PolicyRuleChange {

    PolicyRule policyRule;
    boolean policyRuleActive;

    public PolicyRuleChange(PolicyRule policyRule, boolean policyRuleActive) {
        this.policyRule = policyRule;
        this.policyRuleActive = policyRuleActive;
    }

    public PolicyRule getPolicyRule() {
        return policyRule;
    }

    public void setPolicyRule(PolicyRule policyRule) {
        this.policyRule = policyRule;
    }

    public boolean isPolicyRuleActive() {
        return policyRuleActive;
    }

    public void setPolicyRuleActive(boolean policyRuleActive) {
        this.policyRuleActive = policyRuleActive;
    }

    @Override
    public String toString() {
        return "Policy rule change (" + (policyRuleActive ? "activated" : "deactivated") + "): " + policyRule;
    }

}
