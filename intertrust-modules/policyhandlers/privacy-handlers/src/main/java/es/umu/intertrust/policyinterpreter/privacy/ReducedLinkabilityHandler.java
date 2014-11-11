package es.umu.intertrust.policyinterpreter.privacy;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.orbac.ObligationPrecondition;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.MatchUtils;
import es.umu.intertrust.policyinterpreter.policyhandlers.AbstractPolicyHandler;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerConfiguration;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerInitializationException;
import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.privacy.ReducedLinkabilityHandlerConfigurationParser.ProtectedItem;
import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class ReducedLinkabilityHandler extends AbstractPolicyHandler {

    ReducedLinkabilityHandlerConfigurationParser parser;
    Precondition precondition;
    Map<Obligation, SecurityFeature> deployedFeatures;

    @Override
    public void initialize(PolicyHandlerConfiguration configuration) throws PolicyHandlerInitializationException {
        super.initialize(configuration);
        try {
            parser = new ReducedLinkabilityHandlerConfigurationParser();
            parser.parse(getConfigurationFile());
            deployedFeatures = new HashMap<Obligation, SecurityFeature>();
        } catch (ConfigurationParsingException ex) {
            throw new PolicyHandlerInitializationException(ex.getMessage(), ex);
        }
    }

    public List<Precondition> getPreconditions() throws PolicyHandlerException {
        precondition = new ObligationPrecondition(parser.getAction());
        return Collections.singletonList(precondition);
    }

    @Override
    public void preconditionFulfilled(Precondition precondition, Match match) throws PolicyHandlerException {
        if (precondition != this.precondition) {
            throw new PolicyHandlerException("Unexpected precondition: " + precondition);
        }
        Obligation obligation = MatchUtils.getMatchingObligation(match);
        SecurityFeature securityFeature = new SecurityFeature(parser.getCategory(), parser.getType());
        securityFeature.addFunctionality(parser.getMainFunctionality());
        String object = obligation.getObject();
        ProtectedItem protectedItem = parser.getProtectedItems().get(object);
        if (protectedItem == null) {
            throw new PolicyHandlerException("Unknown protected item: " + object);
        }
        securityFeature.setParameter("protected-item", protectedItem.getId());
        String event = obligation.getParameter();
        String targetId = protectedItem.getTargets().get(event);
        if (targetId == null) {
            throw new PolicyHandlerException("Unknown target for event: " + event);
        }
        SDSTarget target = new SDSTarget(targetId);
        getDeploymentManager().addDeploymentFeature(securityFeature, target);
        deployedFeatures.put(obligation, securityFeature);
    }

    @Override
    public void preconditionUnfulfilled(Precondition precondition, Match cancelledMatch) throws PolicyHandlerException {
        Obligation obligation = MatchUtils.getMatchingObligation(cancelledMatch);
        SecurityFeature securityFeature = deployedFeatures.get(obligation);
        if (securityFeature == null) {
            throw new PolicyHandlerException("Unexpected obligation deactivation: " + obligation);
        }
        getDeploymentManager().removeDeploymentFeature(securityFeature);
    }
}
