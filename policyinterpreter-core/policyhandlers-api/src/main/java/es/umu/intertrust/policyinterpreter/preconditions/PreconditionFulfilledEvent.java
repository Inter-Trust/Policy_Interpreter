package es.umu.intertrust.policyinterpreter.preconditions;

/**
 *
 * @author Juanma
 */
public interface PreconditionFulfilledEvent extends PreconditionFullfillmentEvent {

    public Match getMatch();
}
