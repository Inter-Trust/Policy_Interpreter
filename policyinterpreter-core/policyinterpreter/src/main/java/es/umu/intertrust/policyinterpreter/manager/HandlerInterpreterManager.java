package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandler;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionManagementException;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public class HandlerInterpreterManager implements es.umu.intertrust.policyinterpreter.policyhandlers.InterpreterManager {

    InterpreterManager manager;
    PolicyHandler handler;

    public HandlerInterpreterManager(InterpreterManager manager, PolicyHandler handler) {
        this.manager = manager;
        this.handler = handler;
    }

    public void addPrecondition(Precondition precondition) throws PreconditionManagementException {
        manager.addPrecondition(precondition, handler);
    }

    public void addPreconditions(Collection<Precondition> preconditions) throws PreconditionManagementException {
        manager.addPreconditions(preconditions, handler);
    }

    public void removePrecondition(Precondition precondition) throws PreconditionManagementException {
        manager.removePrecondition(precondition, handler);
    }

    public void removePreconditions(Collection<Precondition> preconditions) throws PreconditionManagementException {
        manager.removePreconditions(preconditions, handler);
    }

}
