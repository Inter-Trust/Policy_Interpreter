package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.policyhandlers.ExceptionManager;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandler;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class HandlersExceptionManager implements ExceptionManager {

    public void throwException(PolicyHandlerException exception) {
        throwException(exception, PolicyHandler.class);
    }

    public void throwException(PolicyHandlerException exception, Class reportingClass) {
        Logger.getLogger(reportingClass.getName()).log(Level.SEVERE, exception.getMessage(), exception);
    }

}
