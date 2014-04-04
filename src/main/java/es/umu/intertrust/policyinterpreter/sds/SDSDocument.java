package es.umu.intertrust.policyinterpreter.sds;

import java.util.List;

/**
 *
 * @author Juanma
 */
public class SDSDocument {

    // TODO - This is a dummy SDS document. Replace by the XML object representation.
    List<SecurityFeature> addedFeatures;
    List<SecurityFeature> removedFeatures;

    public SDSDocument(List<SecurityFeature> addedFeatures, List<SecurityFeature> removedFeatures) {
        this.addedFeatures = addedFeatures;
        this.removedFeatures = removedFeatures;
    }

    public List<SecurityFeature> getAddedFeatures() {
        return addedFeatures;
    }

    public List<SecurityFeature> getRemovedFeatures() {
        return removedFeatures;
    }

    @Override
    public String toString() {
        String string = "Security Deployment Specification document:\n"
                + "==================================================\n"
                + "Added features:\n";
        for (SecurityFeature feature : addedFeatures) {
            string += " - " + feature + "\n";
        }
        string += "\nRemoved features:\n";
        for (SecurityFeature feature : removedFeatures) {
            string += " - " + feature + "\n";
        }
        string += "==================================================\n";
        return string;
    }

}
