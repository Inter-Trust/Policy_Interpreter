package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class DependencyOperator implements SecurityFeatureOperator {

    public static final Logger logger = Logger.getLogger(DependencyOperator.class.getName());

    SecurityFeatureOperator mainOperator;
    ObligationField mainObligationField;
    SecurityFeatureOperator defaultDependentOperator;
    Map<String, SecurityFeatureOperator> dependentOperators;
    Map<SecurityFeature, Obligation> mainValues;
    Map<SecurityFeature, Obligation> dependentValues;
    Map<SecurityFeature, SecurityFeatureOperator> dependentValueOperators;

    public DependencyOperator(SecurityFeatureOperator mainOperator, ObligationField mainObligationField, Map<String, SecurityFeatureOperator> dependentOperators) {
        this(mainOperator, mainObligationField, dependentOperators, null);
    }

    public DependencyOperator(SecurityFeatureOperator mainOperator, ObligationField mainObligationField, Map<String, SecurityFeatureOperator> dependentOperators, SecurityFeatureOperator defaultDependentOperator) {
        this.mainOperator = mainOperator;
        this.mainObligationField = mainObligationField;
        this.defaultDependentOperator = defaultDependentOperator;
        this.dependentOperators = dependentOperators;
        this.mainValues = new HashMap<SecurityFeature, Obligation>();
        this.dependentValues = new HashMap<SecurityFeature, Obligation>();
        this.dependentValueOperators = new HashMap<SecurityFeature, SecurityFeatureOperator>();
    }

    public boolean isOperable(Obligation obligation) {
        if (mainOperator.isOperable(obligation)) {
            return true;
        } else {
            return (getOperableDependent(obligation) != null);
        }
    }

    public boolean processDefaults(SecurityFeature feature) throws PolicyHandlerException {
        logger.log(Level.FINEST, "DependencyOperator processing defaults.");
        boolean modified = mainOperator.processDefaults(feature);
        if (defaultDependentOperator != null) {
            modified = modified | defaultDependentOperator.processDefaults(feature);
            dependentValueOperators.put(feature, defaultDependentOperator);
        }
        return modified;
    }

    public boolean processActivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        logger.log(Level.FINEST, "DependencyOperator processing activation.");
        boolean processed = false;
        boolean modified = false;
        if (mainOperator.isOperable(obligation)) { // The obligation affects the main operator
            logger.log(Level.FINEST, "The obligation affects the main operator.");
            processed = true;
            Obligation mainValue = mainValues.get(feature);
            if (mainValue == null) { // Main value not defined by policy
                logger.log(Level.FINEST, "Main value not defined by policy.");
                modified = modified | mainOperator.clean(feature); // Clean in case a default main value was set by a dependent value
            }
            modified = modified | mainOperator.processActivation(obligation, feature);
            mainValues.put(feature, obligation);
            SecurityFeatureOperator dependentOperator = dependentOperators.get(mainObligationField.getValue(obligation));
            SecurityFeatureOperator currentDependentOperator = dependentValueOperators.get(feature);
            if (currentDependentOperator != null) {
                modified = modified | currentDependentOperator.clean(feature); // Clean in case the new main's dependent operator differs from the previous main's dependent operator or in case no dependent operator is available for the new main
                dependentValueOperators.remove(feature);
            }
            if (dependentOperator != null) { // There are dependent values defined for the main value
                logger.log(Level.FINEST, "There are dependent values defined for the main value.");
                Obligation dependentValue = dependentValues.get(feature);
                if (dependentValue == null) { // No dependent value defined by policy
                    logger.log(Level.FINEST, "No dependent value defined by policy.");
                    modified = modified | dependentOperator.processDefaults(feature);
                } else { // A dependent value has been already defined by policy
                    logger.log(Level.FINEST, "A dependent value has been already defined by policy.");
                    if (dependentOperator.isOperable(dependentValue)) { // The previously defined dependent value is compatible with the new main value
                        logger.log(Level.FINEST, "The previously defined dependent value is compatible with the new main value.");
                        modified = modified | dependentOperator.processActivation(dependentValue, feature);
                        dependentValueOperators.put(feature, dependentOperator);
                    } else {
                        throw new PolicyHandlerException("Incompatible values: " + dependentValue + " and " + obligation);
                    }
                }
            } else { // There are no dependent values defined for the main value
                logger.log(Level.FINEST, "There are no dependent values defined for the main value.");
            }
        }
        // Process obligation for dependent operator
        Map.Entry<String, SecurityFeatureOperator> operableDependent = getOperableDependent(obligation);
        if (operableDependent != null) { // The obligation affects a dependent operator
            logger.log(Level.FINEST, "The Obligation affects a dependent operator.");
            processed = true;
            Obligation mainValue = mainValues.get(feature);
            if (mainValue == null) { // No main value defined by policy
                logger.log(Level.FINEST, "No main value defined by policy.");
                SecurityFeatureOperator currentDependentOperator = dependentValueOperators.get(feature);
                if ((currentDependentOperator != null) && currentDependentOperator.isOperable(obligation)) { // A dependent operator has been already selected and supports the obligation
                    logger.log(Level.FINEST, "A dependent operator has been already selected and supports the obligation.");
                    modified = modified | currentDependentOperator.processActivation(obligation, feature);
                    dependentValues.put(feature, obligation);
                } else { // No dependent operator has been previously selected or it does not support the obligation
                    logger.log(Level.FINEST, "No dependent operator has been previously selected or it does not support the obligation.");
                    if (currentDependentOperator != null) {
                        modified = modified | currentDependentOperator.clean(feature); // Clean because a new operator will be selected
                    }
                    if ((defaultDependentOperator != null) && defaultDependentOperator.isOperable(obligation)) { // The obligation is supported by the dependent operator corresponding to the default main value
                        logger.log(Level.FINEST, "The obligation is supported by the dependent operator corresponding to the default main value.");
                        modified = modified | defaultDependentOperator.processActivation(obligation, feature);
                        dependentValues.put(feature, obligation);
                        dependentValueOperators.put(feature, defaultDependentOperator);
                    } else { // The obligation is not supported by the dependent operator corresponding to the default main value or there is no default main value
                        logger.log(Level.FINEST, "The obligation is not supported by the dependent operator corresponding to the default main value or there is no default main value.");
                        mainValue = generateObligation(operableDependent.getKey());
                        SecurityFeatureOperator dependentOperator = operableDependent.getValue();
                        modified = modified | dependentOperator.processActivation(obligation, feature);
                        dependentValues.put(feature, obligation);
                        dependentValueOperators.put(feature, dependentOperator);
                        modified = modified | mainOperator.processActivation(mainValue, feature);
                    }
                }
            } else { // A main value has been already defined by policy
                logger.log(Level.FINEST, "A main value has been already defined by policy.");
                SecurityFeatureOperator dependentOperator = dependentOperators.get(mainObligationField.getValue(mainValue));
                if (dependentOperator.isOperable(obligation)) { // The obligation is supported by the dependent operator corresponding to the previously defined main value
                    logger.log(Level.FINEST, "The obligation is supported by the dependent operator corresponding to the previously defined main value.");
                    modified = modified | dependentOperator.processActivation(obligation, feature);
                    dependentValues.put(feature, obligation);
                    dependentValueOperators.put(feature, dependentOperator);
                } else {
                    throw new PolicyHandlerException("Incompatible values: " + mainValue + " and " + obligation);
                }
            }
        }
        if (!processed) {
            throw new PolicyHandlerException("Unsupported value: " + obligation);
        }
        return modified;
    }

    public boolean processDeactivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        logger.log(Level.FINEST, "DependencyOperator processing deactivation.");
        boolean processed = false;
        boolean modified = false;
        if (mainOperator.isOperable(obligation)) { // The obligation affects the main operator
            logger.log(Level.FINEST, "The obligation affects the main operator.");
            processed = true;
            mainValues.remove(feature);
            Obligation dependentValue = dependentValues.get(feature);
            if (dependentValue != null) { // Dependent value defined by policy
                logger.log(Level.FINEST, "Dependent value defined by policy.");
                if ((defaultDependentOperator != null) && defaultDependentOperator.isOperable(dependentValue)) { // Dependent value is supported by the dependent operator corresponding to the default main value
                    logger.log(Level.FINEST, "Dependent value is supported by the dependent operator corresponding to the default main value.");
                    modified = modified | mainOperator.processDeactivation(obligation, feature);
                    modified = modified | mainOperator.processDefaults(feature);
                    SecurityFeatureOperator currentDependentOperator = dependentValueOperators.get(feature);
                    if (currentDependentOperator != null) {
                        modified = modified | currentDependentOperator.clean(feature);
                    }
                    modified = modified | defaultDependentOperator.processActivation(dependentValue, feature);
                    dependentValueOperators.put(feature, defaultDependentOperator);
                } else { // Dependent value is defined by policy but not compatible with default main --> let current dependent and main values
                    logger.log(Level.FINEST, "Dependent value is defined by policy but not compatible with default main. Leaving current dependent and main values.");
                }
            } else { // No dependent value defined by policy
                logger.log(Level.FINEST, "No dependent value defined by policy.");
                modified = modified | mainOperator.processDeactivation(obligation, feature);
                SecurityFeatureOperator currentDependentOperator = dependentValueOperators.get(feature);
                if (currentDependentOperator != null) {
                    modified = modified | currentDependentOperator.clean(feature);// Clean in case the default main's dependent operator differs from the previous main's dependent operator (default values would not match)
                }
                modified = modified | processDefaults(feature);
            }
        }
        SecurityFeatureOperator currentDependentOperator = dependentValueOperators.get(feature);
        if ((currentDependentOperator != null) && currentDependentOperator.isOperable(obligation)) { // The obligation affects a dependent operator
            logger.log(Level.FINEST, "The obligation affects a dependent operator.");
            processed = true;
            modified = modified | currentDependentOperator.processDeactivation(obligation, feature);
            dependentValues.remove(feature);
            dependentValueOperators.remove(feature);
            modified = modified | currentDependentOperator.clean(feature); // Clean in case the new dependent operator differs in default values
            Obligation mainValue = mainValues.get(feature);
            if (mainValue != null) { // Main value defined by policy
                logger.log(Level.FINEST, "Main value defined by policy.");
                SecurityFeatureOperator dependentOperator = dependentOperators.get(mainObligationField.getValue(mainValue));
                modified = modified | dependentOperator.processDefaults(feature);
            } else { // No main value defined by policy
                logger.log(Level.FINEST, "No main value defined by policy.");
                modified = modified | processDefaults(feature);
            }
        }
        if (!processed) {
            throw new PolicyHandlerException("Unsupported: " + obligation);
        }
        return modified;
    }

    public boolean clean(SecurityFeature feature) throws PolicyHandlerException {
        logger.log(Level.FINEST, "DependencyOperator processing clean.");
        boolean modified = mainOperator.clean(feature);
        SecurityFeatureOperator currentDependentOperator = dependentValueOperators.get(feature);
        if (currentDependentOperator != null) {
            modified = modified | currentDependentOperator.clean(feature);
        }
        return modified;
    }

    private Map.Entry<String, SecurityFeatureOperator> getOperableDependent(Obligation obligation) {
        for (Map.Entry<String, SecurityFeatureOperator> entry : dependentOperators.entrySet()) {
            if (entry.getValue().isOperable(obligation)) {
                return entry;
            }
        }
        return null;
    }

    private Obligation generateObligation(String value) {
        Obligation obligation = new Obligation("", "", "", "");
        mainObligationField.setValue(obligation, value);
        return obligation;
    }

    @Override
    public String toString() {
        String string = "DependencyOperator:"
                + "\n - Main operator: " + mainOperator
                + "\n - DependentOperators:";
        for (Map.Entry<String, SecurityFeatureOperator> entry : dependentOperators.entrySet()) {
            string += "\n    - " + entry.getKey() + ": " + entry.getValue();
        }
        return string;
    }
}
