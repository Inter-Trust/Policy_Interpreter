package es.umu.intertrust.policyinterpreter.dummy;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policy.AbstractPolicyNotificationHandler;

/**
 *
 * @author Juanma
 */
public class DummyPolicyNotificationHandler extends AbstractPolicyNotificationHandler {

    public void start() {
        Obligation obligation = new Obligation("Name", "subject", "action", "object");

        firePolicyRuleActivated(obligation);
//        firePolicyRuleDeactivated(obligation);
        firePolicyRuleDeactivated(new Obligation("Name", "subject", "action", "object"));

    }

}
