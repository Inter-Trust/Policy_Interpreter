package es.umu.intertrust.policyinterpreter.policy;

import java.util.List;

/**
 *
 * @author Juanma
 */
public interface PolicyChangeListener {

    public void policyStatusChanged(List<PolicyRuleChange> policyRuleChanges);
}
