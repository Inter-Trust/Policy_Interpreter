package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser.ConfigurationProperty;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public abstract class AbstractDependencyOperatorBuilder {

    ConfigurationProperty configurationProperty;
    ObligationField mainObligationField;

    public AbstractDependencyOperatorBuilder(ConfigurationProperty configurationProperty, ObligationField mainObligationField) {
        this.configurationProperty = configurationProperty;
        this.mainObligationField = mainObligationField;
    }

    public SecurityFeatureOperator build() {
        SecurityFeatureOperator mainOperator = generateMainOperator(configurationProperty);
        Map<String, SecurityFeatureOperator> dependentOperators = new HashMap<String, SecurityFeatureOperator>();
        SecurityFeatureOperator defaultDependentOperator = null;
        SDSHandlerConfigurationParser.ConfigurationProperty defaultDependentProperty = configurationProperty.getDefaultDependentProperty();
        for (Map.Entry<String, SDSHandlerConfigurationParser.ConfigurationProperty> entry : configurationProperty.getDependentProperties().entrySet()) {
            String mainValue = entry.getKey();
            SDSHandlerConfigurationParser.ConfigurationProperty dependentProperty = entry.getValue();
            SecurityFeatureOperator dependentOperator = generateDependentOperator(mainValue, dependentProperty);
            if (dependentOperator != null) {
                dependentOperators.put(mainValue, dependentOperator);
                if (dependentProperty == defaultDependentProperty) {
                    defaultDependentOperator = dependentOperator;
                }
            }
        }
        if (dependentOperators.isEmpty()) {
            return mainOperator;
        } else {
            return new DependencyOperator(mainOperator, mainObligationField, dependentOperators, defaultDependentOperator);
        }
    }

    protected abstract SecurityFeatureOperator generateMainOperator(ConfigurationProperty mainProperty);

    protected abstract SecurityFeatureOperator generateDependentOperator(String mainValue, ConfigurationProperty dependentProperty);

}
