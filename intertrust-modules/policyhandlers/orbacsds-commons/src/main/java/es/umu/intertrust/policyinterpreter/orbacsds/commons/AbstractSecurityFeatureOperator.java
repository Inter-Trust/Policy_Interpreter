package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.Configuration;
import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public abstract class AbstractSecurityFeatureOperator implements SecurityFeatureOperator {

    public static final Logger logger = Logger.getLogger(AbstractSecurityFeatureOperator.class.getName());

    boolean checkAlreadySet;
    ObligationField obligationField;
    List<SecurityFeature> securityFeatures;

    public AbstractSecurityFeatureOperator(ObligationField obligationField) {
        this.checkAlreadySet = Boolean.parseBoolean(Configuration.getInstance().getDeploymentProperties().getProperty(Constants.CHECK_EXTERNAL_CHANGES_PROPERTY, Constants.DEFAULT_CHECK_ALREADY_SET_VALUE));
        this.obligationField = obligationField;
        this.securityFeatures = new ArrayList<SecurityFeature>();
    }

    public boolean processDefaults(SecurityFeature feature) throws PolicyHandlerException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0} processing defaults:", this.getClass().getSimpleName());
        }
        String currentValue = getCurrentValue(feature);
        String defaultValue = getDefaultValue();
        checkAlreadySet(feature, currentValue, defaultValue);
        boolean modified = processValue(feature, currentValue, defaultValue);
        securityFeatures.remove(feature);
        return modified;
    }

    public boolean processActivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0} processing activation:", this.getClass().getSimpleName());
        }
        String currentValue = getCurrentValue(feature);
        checkAlreadySet(feature, currentValue, getDefaultValue());
        String value = obligationField.getValue(obligation);
        boolean modified = processValue(feature, currentValue, getValueForObligationValue(value));
        securityFeatures.add(feature);
        return modified;
    }

    public boolean processDeactivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0} processing deactivation:", this.getClass().getSimpleName());
        }
        String value = obligationField.getValue(obligation);
        getValueForObligationValue(value); // Checks whether the value is supported
        boolean modified = processValue(feature, getCurrentValue(feature), null);
        boolean defaultsModified = processDefaults(feature);
        return (modified || defaultsModified);
    }

    public boolean clean(SecurityFeature feature) throws PolicyHandlerException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0} processing clean:", this.getClass().getSimpleName());
        }
        boolean set = processValue(feature, getCurrentValue(feature), null);
        securityFeatures.remove(feature);
        return set;
    }

    protected abstract String getDefaultValue();

    protected abstract String getValueForObligationValue(String value);

    protected abstract String getCurrentValue(SecurityFeature feature);

    protected abstract void setValue(SecurityFeature feature, String currentValue, String newValue);

    protected abstract void removeValue(SecurityFeature feature, String currentValue);

    private void checkAlreadySet(SecurityFeature feature, String currentValue, String defaultValue) throws PolicyHandlerException {
        if (checkAlreadySet && (currentValue != null) && (!currentValue.equals(defaultValue) || securityFeatures.contains(feature))) {
            throw new PolicyHandlerException("Value already set in feature: " + currentValue);
        }
    }

    private boolean processValue(SecurityFeature feature, String currentValue, String newValue) {
        logger.log(Level.FINEST, "{0} -> {1}", new Object[]{currentValue, newValue});
        if ((newValue != null) && !newValue.equals(currentValue)) {
            setValue(feature, currentValue, newValue);
            return true;
        } else if ((newValue == null) && (currentValue != null)) {
            removeValue(feature, currentValue);
            return true;
        } else {
            return false;
        }
    }

}
