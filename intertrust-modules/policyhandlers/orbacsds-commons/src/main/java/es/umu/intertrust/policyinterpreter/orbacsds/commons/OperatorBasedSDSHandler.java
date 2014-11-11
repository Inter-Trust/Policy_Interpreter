package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationPrecondition;
import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerConfiguration;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerInitializationException;
import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public abstract class OperatorBasedSDSHandler extends AbstractSDSHandler {

    public static final Logger logger = Logger.getLogger(OperatorBasedSDSHandler.class.getName());

    Map<String, SDSTarget> targetsMap;
    SecurityFeatureOperator operator;

    @Override
    public void initialize(PolicyHandlerConfiguration configuration) throws PolicyHandlerInitializationException {
        super.initialize(configuration);
        try {
            targetsMap = getTargetsMap();
            operator = getSecurityFeatureOperator();
        } catch (PolicyHandlerException ex) {
            throw new PolicyHandlerInitializationException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Precondition> getGeneralPreconditions() throws PolicyHandlerException {
        return new ArrayList<Precondition>(getObligationPreconditions());
    }

    @Override
    public List<Precondition> generateTargetPreconditions(Precondition precondition, Match match) throws PolicyHandlerException {
        ObligationPrecondition obligationPrecondition = (ObligationPrecondition) precondition;
        if (obligationPrecondition.getObject() != null) {
            throw new PolicyHandlerException("Unexpected obligation precondition with object: " + obligationPrecondition.getObject());
        }
        String object = MatchUtils.getMatchingObligation(match).getObject();
        Precondition targetPrecondition = new ObligationPrecondition(obligationPrecondition.getSubject(), obligationPrecondition.getAction(), object, obligationPrecondition.getParameter());
        return Collections.singletonList(targetPrecondition);
    }

    @Override
    public String getTargetId(Precondition precondition, Match match) throws PolicyHandlerException {
        return MatchUtils.getMatchingObligation(match).getObject();
    }

    @Override
    public List<SecurityFeature> processFirstActivation(Precondition precondition, Match match) throws PolicyHandlerException {
        SecurityFeature securityFeature = new SecurityFeature(getCategory(), getType());
        Obligation obligation = MatchUtils.getMatchingObligation(match);
        operator.processDefaults(securityFeature);
        operator.processActivation(obligation, securityFeature);
        String object = obligation.getObject();
        SDSTarget target = getTarget(object);
        getDeploymentManager().addDeploymentFeature(securityFeature, target);
        return Collections.singletonList(securityFeature);
    }

    @Override
    public void processActivation(Precondition precondition, List<Match> createdMatches, List<Match> oldMatches, List<SecurityFeature> securityFeatures) throws PolicyHandlerException {
        if (securityFeatures.size() != 1) {
            throw new PolicyHandlerException("Unexpected number of security features: " + securityFeatures.size());
        }
        SecurityFeature securityFeature = securityFeatures.get(0);
        for (Match match : createdMatches) {
            Obligation obligation = MatchUtils.getMatchingObligation(match);
            boolean modified = operator.processActivation(obligation, securityFeature);
            logger.log(Level.FINEST, "Security feature modified: {0}", modified);
            if (modified) {
                getDeploymentManager().updateDeploymentFeature(securityFeature);
            }
        }
    }

    @Override
    public void processDeactivation(Precondition precondition, List<Match> cancelledMatches, List<Match> activeMatches, List<SecurityFeature> securityFeatures) throws PolicyHandlerException {
        if (securityFeatures.size() != 1) {
            throw new PolicyHandlerException("Unexpected number of security features: " + securityFeatures.size());
        }
        SecurityFeature securityFeature = securityFeatures.get(0);
        boolean modified = false;
        for (Match match : cancelledMatches) {
            Obligation obligation = MatchUtils.getMatchingObligation(match);
            modified = modified | operator.processDeactivation(obligation, securityFeature);
        }
        logger.log(Level.FINEST, "Security feature modified: {0}", modified);
        if (modified) {
            getDeploymentManager().updateDeploymentFeature(securityFeature);
        }
    }

    @Override
    public void processLastDeactivation(Precondition precondition, Match cancelledMatch, List<SecurityFeature> securityFeatures) throws PolicyHandlerException {
        if (securityFeatures.size() != 1) {
            throw new PolicyHandlerException("Unexpected number of security features: " + securityFeatures.size());
        }
        getDeploymentManager().removeDeploymentFeature(securityFeatures.get(0));
    }

    public abstract List<ObligationPrecondition> getObligationPreconditions() throws PolicyHandlerException;

    public abstract String getCategory() throws PolicyHandlerException;

    public abstract String getType() throws PolicyHandlerException;

    public abstract Map<String, SDSTarget> getTargetsMap() throws PolicyHandlerException;

    public abstract SecurityFeatureOperator getSecurityFeatureOperator() throws PolicyHandlerException;

    private SDSTarget getTarget(String value) throws PolicyHandlerException {
        SDSTarget target = targetsMap.get(value);
        if (target == null) {
            throw new PolicyHandlerException("No matching target found: " + value);
        }
        return target;
    }

}
