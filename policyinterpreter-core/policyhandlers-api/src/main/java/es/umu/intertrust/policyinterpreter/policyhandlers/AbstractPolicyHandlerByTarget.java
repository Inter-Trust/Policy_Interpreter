package es.umu.intertrust.policyinterpreter.policyhandlers;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.preconditions.PreconditionManagementException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public abstract class AbstractPolicyHandlerByTarget<T extends DeploymentFeature> extends AbstractPolicyHandler {

    public static final Logger logger = Logger.getLogger(AbstractPolicyHandlerByTarget.class.getName());

    List<Precondition> generalPreconditions;
    Map<Precondition, List<String>> handledTargets;
    Map<Precondition, Precondition> generalPreconditionByTargetPrecondition;
    Map<Precondition, List<T>> deployedFeatures;

    public AbstractPolicyHandlerByTarget() {
        this.handledTargets = new HashMap<Precondition, List<String>>();
        this.deployedFeatures = new HashMap<Precondition, List<T>>();
        this.generalPreconditionByTargetPrecondition = new HashMap<Precondition, Precondition>();
    }

    @Override
    public List<Precondition> getPreconditions() throws PolicyHandlerException {
        this.generalPreconditions = getGeneralPreconditions();
        return generalPreconditions;
    }

    @Override
    public void preconditionFulfilled(Precondition precondition, Match match) throws PolicyHandlerException {
        logger.log(Level.FINEST, "Precondition fulfilled: {0}", precondition);
        if (generalPreconditions.contains(precondition)) {
            generalPreconditionMatched(precondition, match);
        } else {
            logger.log(Level.FINEST, "Processing first activation.");
            List<T> deploymentFeatures = processFirstActivation(precondition, match);
            deployedFeatures.put(precondition, deploymentFeatures);
        }
    }

    @Override
    public void preconditionUnfulfilled(Precondition precondition, Match cancelledMatch) throws PolicyHandlerException {
        logger.log(Level.FINEST, "Precondition unfulfilled: {0}", precondition);
        if (!generalPreconditions.contains(precondition)) {
            List<T> deploymentFeatures = getDeployedFeatures(precondition);
            logger.log(Level.FINEST, "Processing last deactivation.");
            processLastDeactivation(precondition, cancelledMatch, deploymentFeatures);
            deployedFeatures.remove(precondition);
            try {
                Precondition generalPrecondition = generalPreconditionByTargetPrecondition.get(precondition);
                getInterpreterManager().removePrecondition(precondition);
                generalPreconditionByTargetPrecondition.remove(precondition);
                String targetId = getTargetId(precondition, cancelledMatch);
                List<String> preconditionTargetIds = handledTargets.get(generalPrecondition);
                if (preconditionTargetIds != null) {
                    preconditionTargetIds.remove(targetId);
                }
            } catch (PreconditionManagementException ex) {
                throw new PolicyHandlerException(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void fulfilledPreconditionMatchesCreated(Precondition precondition, List<Match> createdMatches, List<Match> oldMatches) throws PolicyHandlerException {
        logger.log(Level.FINEST, "Precondition matches created: {0}", precondition);
        if (generalPreconditions.contains(precondition)) {
            for (Match match : createdMatches) {
                generalPreconditionMatched(precondition, match);
            }
        } else {
            List<T> deploymentFeatures = getDeployedFeatures(precondition);
            logger.log(Level.FINEST, "Processing activation.");
            processActivation(precondition, createdMatches, oldMatches, deploymentFeatures);
        }
    }

    @Override
    public void fulfilledPreconditionMatchesCancelled(Precondition precondition, List<Match> cancelledMatches, List<Match> activeMatches) throws PolicyHandlerException {
        logger.log(Level.FINEST, "Precondition matches cancelled: {0}", precondition);
        if (!generalPreconditions.contains(precondition)) {
            List<T> deploymentFeatures = getDeployedFeatures(precondition);
            logger.log(Level.FINEST, "Processing deactivation.");
            processDeactivation(precondition, cancelledMatches, activeMatches, deploymentFeatures);
        }
    }

    public abstract List<Precondition> getGeneralPreconditions() throws PolicyHandlerException;

    public abstract List<Precondition> generateTargetPreconditions(Precondition precondition, Match match) throws PolicyHandlerException;

    public abstract String getTargetId(Precondition precondition, Match match) throws PolicyHandlerException;

    public abstract List<T> processFirstActivation(Precondition precondition, Match match) throws PolicyHandlerException;

    public abstract void processActivation(Precondition precondition, List<Match> createdMatches, List<Match> oldMatches, List<T> deploymentFeatures) throws PolicyHandlerException;

    public abstract void processDeactivation(Precondition precondition, List<Match> cancelledMatches, List<Match> activeMatches, List<T> deploymentFeatures) throws PolicyHandlerException;

    public abstract void processLastDeactivation(Precondition precondition, Match cancelledMatch, List<T> deploymentFeatures) throws PolicyHandlerException;

    private void generalPreconditionMatched(Precondition precondition, Match match) throws PolicyHandlerException {
        logger.log(Level.FINEST, "General precondition matched: {0}", precondition);
        try {
            List<String> preconditionTargetIds = handledTargets.get(precondition);
            if (preconditionTargetIds == null) {
                preconditionTargetIds = new ArrayList<String>();
                handledTargets.put(precondition, preconditionTargetIds);
            }
            String targetId = getTargetId(precondition, match);
            if (!preconditionTargetIds.contains(targetId)) {
                List<Precondition> targetPreconditions = generateTargetPreconditions(precondition, match);
                getInterpreterManager().addPreconditions(targetPreconditions);
                for (Precondition targetPrecondition : targetPreconditions) {
                    generalPreconditionByTargetPrecondition.put(targetPrecondition, precondition);
                }
                preconditionTargetIds.add(targetId);
            }
        } catch (PreconditionManagementException ex) {
            throw new PolicyHandlerException(ex.getMessage(), ex);
        }
    }

    private List<T> getDeployedFeatures(Precondition precondition) throws PolicyHandlerException {
        List<T> deploymentFeatures = deployedFeatures.get(precondition);
        return deploymentFeatures;
    }

}
