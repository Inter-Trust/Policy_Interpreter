package es.umu.intertrust.policyinterpreter.policy;

import java.util.Properties;

/**
 *
 * @author Juanma
 */
public interface PolicyNotificationHandler {

    public void initialize(Properties policyProperties) throws PolicyNotificationHandlerException;

    public void start() throws PolicyNotificationHandlerException;

    public void stop() throws PolicyNotificationHandlerException;

    public void addPolicyChangeListener(PolicyChangeListener listener);

    public void removePolicyChangeListener(PolicyChangeListener listener);

    public void addPolicyNotificationHandlerListener(PolicyNotificationHandlerListener listener);

    public void removePolicyNotificationHandlerListener(PolicyNotificationHandlerListener listener);

    public void addExceptionListener(ExceptionListener listener);

    public void removeExceptionListener(ExceptionListener listener);

}
