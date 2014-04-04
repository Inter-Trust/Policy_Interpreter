package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class SDSGenerator {

    public SDSDocument generate(List<DeploymentFeature> addedFeatures, List<DeploymentFeature> removedFeatures) throws SDSGenerationException {
        List<SecurityFeature> addedSecurityFeatures = new ArrayList<SecurityFeature>();
        for (DeploymentFeature feature : addedFeatures) {
            if (feature instanceof SecurityFeature) {
                addedSecurityFeatures.add((SecurityFeature) feature);
            } else {
                throw new SDSGenerationException("Unsupported feature type: " + feature.getClass());
            }
        }
        List<SecurityFeature> removedSecurityFeatures = new ArrayList<SecurityFeature>();
        for (DeploymentFeature feature : removedFeatures) {
            if (feature instanceof SecurityFeature) {
                removedSecurityFeatures.add((SecurityFeature) feature);
            } else {
                throw new SDSGenerationException("Unsupported feature type: " + feature.getClass());
            }
        }
        return new SDSDocument(addedSecurityFeatures, removedSecurityFeatures);
    }
}
