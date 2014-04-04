package es.umu.intertrust.policyinterpreter.policy;

/**
 *
 * @author Juanma
 */
public interface PolicyNotificationHandler {

    public void start();

    public void addPolicyChangeListener(PolicyChangeListener listener);

    public void removePolicyChangeListener(PolicyChangeListener listener);
}
