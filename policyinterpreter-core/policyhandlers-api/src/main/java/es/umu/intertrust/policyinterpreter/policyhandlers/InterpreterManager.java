package es.umu.intertrust.policyinterpreter.policyhandlers;

import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionManagementException;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public interface InterpreterManager {

    public void addPrecondition(Precondition precondition) throws PreconditionManagementException;

    public void addPreconditions(Collection<Precondition> preconditions) throws PreconditionManagementException;

    public void removePrecondition(Precondition precondition) throws PreconditionManagementException;

    public void removePreconditions(Collection<Precondition> preconditions) throws PreconditionManagementException;

}
