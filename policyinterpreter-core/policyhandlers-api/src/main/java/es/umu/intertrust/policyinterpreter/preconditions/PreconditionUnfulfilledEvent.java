package es.umu.intertrust.policyinterpreter.preconditions;

/**
 *
 * @author Juanma
 */
public interface PreconditionUnfulfilledEvent extends PreconditionFullfillmentEvent {

    public Match getCancelledMatch();
}
