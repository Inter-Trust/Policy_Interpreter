package es.umu.intertrust.policyinterpreter.privacy;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.orbac.ObligationPrecondition;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.MatchUtils;
import es.umu.intertrust.policyinterpreter.policyhandlers.AbstractPolicyHandler;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerConfiguration;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerInitializationException;
import es.umu.intertrust.policyinterpreter.policyhandlers.UnsupportedQueryManagerException;
import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.preconditions.Precondition;
import es.umu.intertrust.policyinterpreter.privacy.PseudonymityHandlerConfigurationParser.DataTarget;
import es.umu.intertrust.policyinterpreter.privacy.PseudonymityHandlerConfigurationParser.ProtectedItem;
import es.umu.intertrust.policyinterpreter.sds.SDSQueryManager;
import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import es.umu.intertrust.policyinterpreter.sds.filters.IdFilter;
import es.umu.intertrust.policyinterpreter.util.collections.ListHashMap;
import es.umu.intertrust.policyinterpreter.util.collections.ListMap;
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
public class PseudonymityHandler extends AbstractPolicyHandler {

    public static final Logger logger = Logger.getLogger(PseudonymityHandler.class.getName());

    String action;
    String category;
    String type;
    String mainFunctionality;
    String encryptionMainFunctionality;
    String encryptionParameterName;
    String encryptionParameterValue;
    String signatureMainFunctionality;
    String signatureParameterName;
    String signatureParameterValue;
    Map<String, ProtectedItem> protectedItems;

    ObligationPrecondition generalPrecondition;
    List<Precondition> dataTargetPreconditions;
    List<Precondition> encryptionTargetPreconditions;
    List<Precondition> signatureTargetPreconditions;

    Map<SecurityFeature, String> encryptionOriginalValues;
    Map<SecurityFeature, String> signatureOriginalValues;
    ListMap<Precondition, SecurityFeature> deployedFeatures;

    @Override
    public void initialize(PolicyHandlerConfiguration configuration) throws PolicyHandlerInitializationException {
        super.initialize(configuration);
        try {
            PseudonymityHandlerConfigurationParser parser = new PseudonymityHandlerConfigurationParser();
            parser.parse(getConfigurationFile());
            action = parser.getAction();
            category = parser.getCategory();
            type = parser.getType();
            mainFunctionality = parser.getMainFunctionality();
            encryptionMainFunctionality = parser.getEncryptionMainFunctionality();
            encryptionParameterName = parser.getEncryptionParameterName();
            encryptionParameterValue = parser.getEncryptionParameterValue();
            signatureMainFunctionality = parser.getSignatureMainFunctionality();
            signatureParameterName = parser.getSignatureParameterName();
            signatureParameterValue = parser.getSignatureParameterValue();
            protectedItems = parser.getProtectedItems();

            dataTargetPreconditions = new ArrayList<Precondition>();
            encryptionTargetPreconditions = new ArrayList<Precondition>();
            signatureTargetPreconditions = new ArrayList<Precondition>();
            encryptionOriginalValues = new HashMap<SecurityFeature, String>();
            signatureOriginalValues = new HashMap<SecurityFeature, String>();
            deployedFeatures = new ListHashMap<Precondition, SecurityFeature>();
        } catch (ConfigurationParsingException ex) {
            throw new PolicyHandlerInitializationException(ex.getMessage(), ex);
        }
    }

    public List<Precondition> getPreconditions() throws PolicyHandlerException {
        List<Precondition> preconditions = new ArrayList<Precondition>();
        for (Map.Entry<String, ProtectedItem> protectedItemEntry : protectedItems.entrySet()) {
            String target = protectedItemEntry.getKey();
            ProtectedItem protectedItem = protectedItemEntry.getValue();
            if (!protectedItem.getDataTargets().isEmpty()) {
                Precondition targetPrecondition = new ObligationPrecondition(action, target);
                dataTargetPreconditions.add(targetPrecondition);
                preconditions.add(targetPrecondition);
            }
            for (String encryptionTarget : protectedItem.getEncryptionTargets()) {
                Precondition targetPrecondition = new Precondition(generateEvaluationCondition(action, target, encryptionMainFunctionality, encryptionTarget));
                encryptionTargetPreconditions.add(targetPrecondition);
                preconditions.add(targetPrecondition);
            }
            for (String signatureTarget : protectedItem.getSignatureTargets()) {
                Precondition targetPrecondition = new Precondition(generateEvaluationCondition(action, target, signatureMainFunctionality, signatureTarget));
                signatureTargetPreconditions.add(targetPrecondition);
                preconditions.add(targetPrecondition);
            }
        }
        return preconditions;
    }

    @Override
    public void preconditionFulfilled(Precondition precondition, Match match) throws PolicyHandlerException {
        logger.log(Level.FINEST, "Processing first activation: {0}", match);
        if (dataTargetPreconditions.contains(precondition)) {
            Obligation obligation = MatchUtils.getMatchingObligation(match);
            logger.log(Level.FINEST, "Processing obligation: {0}", obligation);
            String object = obligation.getObject();
            ProtectedItem protectedItem = protectedItems.get(object);
            if (protectedItem == null) {
                throw new PolicyHandlerException("Unknown protected item: " + object);
            }
            for (DataTarget dataTarget : protectedItem.getDataTargets()) {
                SecurityFeature securityFeature = new SecurityFeature(category, type);
                securityFeature.addFunctionality(mainFunctionality);
                securityFeature.setParameter("protected-item", dataTarget.getId());
                securityFeature.setParameter("pseudonym", dataTarget.getPseudonym());
                SDSTarget target = new SDSTarget(dataTarget.getTarget());
                getDeploymentManager().addDeploymentFeature(securityFeature, target);
                deployedFeatures.addToList(precondition, securityFeature);
            }
        } else if (encryptionTargetPreconditions.contains(precondition)) {
            SecurityFeature securityFeature = MatchUtils.getMatchingSecurityFeature(match);
            logger.log(Level.FINEST, "Processing encryption security feature: {0}", securityFeature);
            if (encryptionOriginalValues.containsKey(securityFeature)) {
                throw new PolicyHandlerException("Security feature already processed to use pseudonyms: " + securityFeature);
            }
            encryptionOriginalValues.put(securityFeature, securityFeature.getParameterValue(encryptionParameterName));
            securityFeature.setParameter(encryptionParameterName, encryptionParameterValue);
            getDeploymentManager().updateDeploymentFeature(securityFeature);
            deployedFeatures.addToList(precondition, securityFeature);
        } else if (signatureTargetPreconditions.contains(precondition)) {
            SecurityFeature securityFeature = MatchUtils.getMatchingSecurityFeature(match);
            logger.log(Level.FINEST, "Processing signature security feature: {0}", securityFeature);
            if (signatureOriginalValues.containsKey(securityFeature)) {
                throw new PolicyHandlerException("Security feature already processed to use pseudonyms: " + securityFeature);
            }
            signatureOriginalValues.put(securityFeature, securityFeature.getParameterValue(signatureParameterName));
            securityFeature.setParameter(signatureParameterName, signatureParameterValue);
            getDeploymentManager().updateDeploymentFeature(securityFeature);
            deployedFeatures.addToList(precondition, securityFeature);
        } else {
            throw new PolicyHandlerException("Unexpected precondition: " + precondition);
        }
    }

    @Override
    public void preconditionUnfulfilled(Precondition precondition, Match cancelledMatch) throws PolicyHandlerException {
        try {
            logger.log(Level.FINEST, "Processing last deactivation: {0}", precondition);
            if (dataTargetPreconditions.contains(precondition)) {
                for (SecurityFeature securityFeature : deployedFeatures.getList(precondition)) {
                    logger.log(Level.FINEST, "Processing pseudonymity security feature: {0}", securityFeature);
                    getDeploymentManager().removeDeploymentFeature(securityFeature);
                }
                deployedFeatures.remove(precondition);
            } else if (encryptionTargetPreconditions.contains(precondition)) {
                for (SecurityFeature securityFeature : deployedFeatures.getList(precondition)) {
                    logger.log(Level.FINEST, "Processing encryption security feature: {0}", securityFeature);
                    SDSQueryManager queryManager = getQueryManager(SDSQueryManager.class);
                    securityFeature = queryManager.getDeploymentFeature(new IdFilter(securityFeature.getId()));
                    if (securityFeature != null) {
                        if (!encryptionParameterValue.equals(securityFeature.getParameterValue(encryptionParameterName))) {
                            throw new PolicyHandlerException("Security feature not using pseudonyms: " + securityFeature);
                        }
                        String originalValue = encryptionOriginalValues.get(securityFeature);
                        securityFeature.setParameter(encryptionParameterName, originalValue);
                        encryptionOriginalValues.remove(securityFeature);
                        getDeploymentManager().updateDeploymentFeature(securityFeature);
                    }
                }
                deployedFeatures.remove(precondition);
            } else if (signatureTargetPreconditions.contains(precondition)) {
                for (SecurityFeature securityFeature : deployedFeatures.getList(precondition)) {
                    logger.log(Level.FINEST, "Processing signature security feature: {0}", securityFeature);
                    SDSQueryManager queryManager = getQueryManager(SDSQueryManager.class);
                    securityFeature = queryManager.getDeploymentFeature(new IdFilter(securityFeature.getId()));
                    if (securityFeature != null) {
                        if (!signatureParameterValue.equals(securityFeature.getParameterValue(signatureParameterName))) {
                            throw new PolicyHandlerException("Security feature not using pseudonyms: " + securityFeature);
                        }
                        String originalValue = signatureOriginalValues.get(securityFeature);
                        securityFeature.setParameter(signatureParameterName, originalValue);
                        signatureOriginalValues.remove(securityFeature);
                        getDeploymentManager().updateDeploymentFeature(securityFeature);
                    }
                }
                deployedFeatures.remove(precondition);
            } else {
                throw new PolicyHandlerException("Unexpected precondition: " + precondition);
            }
        } catch (UnsupportedQueryManagerException ex) {
            throw new PolicyHandlerException(ex.getMessage(), ex);
        }
    }

    private String generateEvaluationCondition(String obligationAction, String obligationObject, String featureFunctionality, String featureTarget) {
        String evaluationCondition = "$o : Obligation (action==\"" + obligationAction + "\", object==\"" + obligationObject + "\") && "
                + "$sf : SecurityFeature ($func : functionalities) && "
                + "String (this==\"" + featureFunctionality + "\") from $func "
                + "$t : SDSTarget (id==\"" + featureTarget + "\") && "
                + "AssignedTarget(feature==$sf, target==$t)";
        return evaluationCondition;
    }
}
