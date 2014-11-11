package es.umu.intertrust.policyinterpreter.preconditions;

/**
 *
 * @author Juanma
 */
public interface PreconditionFulfillmentListener {

    public void preconditionFulfilled(PreconditionFulfilledEvent evt);

    public void fulfilledPreconditionMatchesCreated(MatchesCreatedEvent evt);

    public void fulfilledPreconditionMatchesCancelled(MatchesCancelledEvent evt);

    public void preconditionUnfulfilled(PreconditionUnfulfilledEvent evt);
}
