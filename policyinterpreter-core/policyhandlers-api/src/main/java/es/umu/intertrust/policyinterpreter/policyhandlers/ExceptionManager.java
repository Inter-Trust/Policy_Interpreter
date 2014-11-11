package es.umu.intertrust.policyinterpreter.policyhandlers;

/**
 *
 * @author Juanma
 */
public interface ExceptionManager {

    public void throwException(PolicyHandlerException exception);

    public void throwException(PolicyHandlerException exception, Class reportingClass);
}
