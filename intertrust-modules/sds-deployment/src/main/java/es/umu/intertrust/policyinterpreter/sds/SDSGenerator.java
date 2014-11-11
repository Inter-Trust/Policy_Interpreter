package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.deployment.Target;
import es.umu.intertrust.policyinterpreter.sds.xml.Category;
import es.umu.intertrust.policyinterpreter.sds.xml.Configuration;
import es.umu.intertrust.policyinterpreter.sds.xml.Deploy;
import es.umu.intertrust.policyinterpreter.sds.xml.Functionality;
import es.umu.intertrust.policyinterpreter.sds.xml.Sds;
import es.umu.intertrust.policyinterpreter.sds.xml.Undeploy;
import es.umu.intertrust.policyinterpreter.sds.xml.UndeploySecurityFeature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class SDSGenerator {

    public static final Logger logger = Logger.getLogger(SDSGenerator.class.getName());

    DeploymentManager manager;

    public SDSGenerator(DeploymentManager manager) {
        this.manager = manager;
    }

    public Sds generate() throws SDSGenerationException {
        Sds xmlSDS = new Sds();
        // Added features
        List<DeploymentFeature> addedFeatures = manager.getAddedFeatures();
        logger.log(Level.FINE, "Added features: {0}", addedFeatures);

        // Updated features
        List<DeploymentFeature> updatedFeatures = new ArrayList<DeploymentFeature>();
        for (DeploymentFeature feature : manager.getUpdatedFeatures()) {
            if (!addedFeatures.contains(feature) && !updatedFeatures.contains(feature)) {
                updatedFeatures.add(feature);
            }
        }
        logger.log(Level.FINE, "Updated features: {0}", updatedFeatures);

        // Features to deploy
        List<DeploymentFeature> featuresToDeploy = new ArrayList<DeploymentFeature>();
        featuresToDeploy.addAll(addedFeatures);
        featuresToDeploy.addAll(updatedFeatures);
        if (!featuresToDeploy.isEmpty()) {
            xmlSDS.setDeploy(generateDeploy(featuresToDeploy));
        }

        // Removed features
        List<DeploymentFeature> removedFeatures = manager.getRemovedFeatures();
        logger.log(Level.FINE, "Removed features: {0}", removedFeatures);
        if (!removedFeatures.isEmpty()) {
            xmlSDS.setUndeploy(generateUndeploy(removedFeatures));
        }
        return xmlSDS;
    }

    private Deploy generateDeploy(List<DeploymentFeature> addedFeatures) throws SDSGenerationException {
        Deploy xmlDeploy = new Deploy();
        List<Category> xmlCategories = xmlDeploy.getCategory();
        Map<String, Category> xmlCategoriesById = new HashMap<String, Category>();
        for (DeploymentFeature feature : addedFeatures) {
            SecurityFeature securityFeature = getSecurityFeature(feature);
            String categoryId = securityFeature.getCategory();
            Category xmlCategory = xmlCategoriesById.get(categoryId);
            if (xmlCategory == null) {
                xmlCategory = new Category();
                xmlCategory.setId(categoryId);
                xmlCategories.add(xmlCategory);
                xmlCategoriesById.put(categoryId, xmlCategory);
            }
            xmlCategory.getSecurityFeature().add(generateSecurityFeature(securityFeature, manager.getTargetsByFeature(feature)));

        }
        return xmlDeploy;
    }

    private Undeploy generateUndeploy(List<DeploymentFeature> removedFeatures) throws SDSGenerationException {
        Undeploy xmlUndeploy = new Undeploy();
        List<UndeploySecurityFeature> xmlUndeploySecurityFeatures = xmlUndeploy.getUndeploySecurityFeature();
        for (DeploymentFeature feature : removedFeatures) {
            SecurityFeature securityFeature = getSecurityFeature(feature);
            UndeploySecurityFeature xmlUndeploySecurityFeature = new UndeploySecurityFeature();
            xmlUndeploySecurityFeature.setId(securityFeature.getId());
            xmlUndeploySecurityFeatures.add(xmlUndeploySecurityFeature);
        }
        return xmlUndeploy;
    }

    private es.umu.intertrust.policyinterpreter.sds.xml.SecurityFeature generateSecurityFeature(SecurityFeature securityFeature, List<Target> targets) {
        es.umu.intertrust.policyinterpreter.sds.xml.SecurityFeature xmlSecurityFeature = new es.umu.intertrust.policyinterpreter.sds.xml.SecurityFeature();
        xmlSecurityFeature.setId(securityFeature.getId());
        xmlSecurityFeature.setType(securityFeature.getType());
        es.umu.intertrust.policyinterpreter.sds.xml.Target xmlTarget = new es.umu.intertrust.policyinterpreter.sds.xml.Target();
        xmlTarget.setId(getTargetId(securityFeature, targets));
        xmlSecurityFeature.setTarget(xmlTarget);
        List<String> functionalities = securityFeature.getFunctionalities();
        if (!functionalities.isEmpty()) {
            List<Functionality> xmlFunctionalities = xmlSecurityFeature.getFunctionality();
            for (String functionality : securityFeature.getFunctionalities()) {
                Functionality xmlFunctionality = new Functionality();
                xmlFunctionality.setId(functionality);
                xmlFunctionalities.add(xmlFunctionality);
            }
        }
        Collection<SecurityParameter> securityParameters = securityFeature.getParameters();
        if (!securityParameters.isEmpty()) {
            Configuration xmlConfiguration = new Configuration();
            xmlSecurityFeature.setConfiguration(xmlConfiguration);
            es.umu.intertrust.policyinterpreter.sds.xml.SecurityParameters xmlSecurityParameters = new es.umu.intertrust.policyinterpreter.sds.xml.SecurityParameters();
            xmlConfiguration.setSecurityParameters(xmlSecurityParameters);
            List<es.umu.intertrust.policyinterpreter.sds.xml.SecurityParameter> xmlParameters = xmlSecurityParameters.getParameter();
            for (SecurityParameter parameter : securityParameters) {
                es.umu.intertrust.policyinterpreter.sds.xml.SecurityParameter xmlSecurityParameter = new es.umu.intertrust.policyinterpreter.sds.xml.SecurityParameter();
                xmlSecurityParameter.setName(parameter.getName());
                xmlSecurityParameter.setValue(parameter.getValue());
                xmlParameters.add(xmlSecurityParameter);
            }
        }
        return xmlSecurityFeature;
    }

    private SecurityFeature getSecurityFeature(DeploymentFeature feature) throws SDSGenerationException {
        if (feature instanceof SecurityFeature) {
            return (SecurityFeature) feature;
        } else {
            throw new SDSGenerationException("Unsupported feature type: " + feature.getClass());
        }
    }

    private String getTargetId(SecurityFeature feature, List<Target> targets) {
        if (targets.isEmpty()) {
            throw new IllegalArgumentException("No target specified for feature: " + feature);
        }
        if (targets.size() > 1) {
            throw new IllegalArgumentException("More than one target found for feature: " + feature);
        }
        Target target = targets.get(0);
        if (target instanceof SDSTarget) {
            return ((SDSTarget) target).getId();
        } else {
            throw new IllegalArgumentException("Invalid target type found for feature: " + feature);
        }
    }
}
