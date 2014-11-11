package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser.ConfigurationProperty;
import java.util.Arrays;

/**
 *
 * Usage:
 *
 * - To build a single-level functionality operator:
 * <code>new SecurityFeatureOperatorBuilder(property).build();</code>
 *
 * - To build a multi-level functionality operator:
 * <code>new SecurityFeatureOperatorBuilder(property).setLevels(0).build();</code>
 *
 * - To build a single-level parameter operator:
 * <code>new SecurityFeatureOperatorBuilder(property).setParameterNames("name").build();</code>
 *
 * - To build a multi-level parameter operator:
 * <code>new SecurityFeatureOperatorBuilder(property).setParameterNames("name1", "name2").build();</code>
 *
 * - To build a parameter operator with short values:
 * <code>new SecurityFeatureOperatorBuilder(property).setOperatorTypes(OperatorType.PARAMETER_WITH_SHORT_VALUES).setParameterNames("name").build();</code>
 *
 * - To build a multi-level operator with functionalies and parameters:
 * <code>new SecurityFeatureOperatorBuilder(property).setParameterNames("name1", null, "name2").build();</code>
 *
 * @author Juanma
 */
public class SecurityFeatureOperatorBuilder {

    public enum OperatorType {

        FUNCTIONALITY, PARAMETER, PARAMETER_WITH_SHORT_VALUES
    }

    final OperatorType DEFAULT_OPERATOR_TYPE = OperatorType.FUNCTIONALITY;
    final ObligationField DEFAULT_OBLIGATION_FIELD = ObligationField.PARAMETER;
    final int DEFAULT_LEVELS = 1;

    ConfigurationProperty configurationProperty;
    int levels;
    String[] parameterNames;
    OperatorType[][] operatorTypes;
    ObligationField[] obligationFields;

    public SecurityFeatureOperatorBuilder(ConfigurationProperty configurationProperty) {
        this.configurationProperty = configurationProperty;
        this.levels = DEFAULT_LEVELS;
        this.parameterNames = new String[0];
        this.operatorTypes = new OperatorType[][]{{DEFAULT_OPERATOR_TYPE}};
        this.obligationFields = new ObligationField[]{DEFAULT_OBLIGATION_FIELD};
    }

    /**
     *
     * @param parameterNames The names of the parameters. If a parameter is
     * null, the corresponding level will be considered as functionality.
     * @return
     */
    public SecurityFeatureOperatorBuilder setParameterNames(String... parameterNames) {
        this.parameterNames = parameterNames;
        this.levels = parameterNames.length;
        OperatorType[][] newOperatorTypes = new OperatorType[levels][];
        OperatorType[] lastOperatorType = new OperatorType[]{OperatorType.PARAMETER};
        for (int i = 0; i < levels; i++) {
            if (parameterNames[i] == null) {
                newOperatorTypes[i] = new OperatorType[]{OperatorType.FUNCTIONALITY};
            } else if (operatorTypes.length <= i) {
                newOperatorTypes[i] = lastOperatorType;
            } else {
                lastOperatorType = operatorTypes[i];
                newOperatorTypes[i] = lastOperatorType;
            }
        }
        this.operatorTypes = newOperatorTypes;
        return this;
    }

    /**
     * Sets operator types (one for each level)
     *
     * @param operatorTypes
     * @return
     */
    public SecurityFeatureOperatorBuilder setOperatorTypes(OperatorType... operatorTypes) {
        this.operatorTypes = new OperatorType[operatorTypes.length][];
        for (int i = 0; i < operatorTypes.length; i++) {
            this.operatorTypes[i] = new OperatorType[]{operatorTypes[i]};
        }
        this.levels = Math.max(levels, operatorTypes.length);
        return this;
    }

    /**
     * Sets multiple operator types for a given level. If only set for level 0,
     * the specified types will apply to all levels.
     *
     * @param level
     * @param operatorTypes
     * @return
     */
    public SecurityFeatureOperatorBuilder setOperatorTypes(int level, OperatorType... operatorTypes) {
        if (level >= this.operatorTypes.length) {
            OperatorType[][] newOperatorTypes = Arrays.copyOf(this.operatorTypes, level + 1);
            Arrays.fill(newOperatorTypes, this.operatorTypes.length, newOperatorTypes.length - 1, this.operatorTypes[this.operatorTypes.length - 1]);
            this.operatorTypes = newOperatorTypes;
        }
        this.operatorTypes[level] = operatorTypes;
        this.levels = Math.max(levels, operatorTypes.length);
        return this;
    }

    /**
     * Sets multiple operator types for each level.
     *
     * @param operatorTypes
     * @return
     */
    public SecurityFeatureOperatorBuilder setMultiOperatorTypes(OperatorType[]  
        ... operatorTypes) {
        this.operatorTypes = operatorTypes;
        this.levels = Math.max(levels, operatorTypes.length);
        return this;
    }

    public SecurityFeatureOperatorBuilder setObligationFields(ObligationField... obligationFields) {
        this.obligationFields = obligationFields;
        this.levels = Math.max(levels, obligationFields.length);
        return this;
    }

    /**
     *
     * @param levels Number of levels to operate or 0 to recursivelly operate
     * all levels.
     * @return
     */
    public SecurityFeatureOperatorBuilder setLevels(int levels) {
        this.levels = levels;
        return this;
    }

    public SecurityFeatureOperator build() {
        return build(configurationProperty, 0);
    }

    private SecurityFeatureOperator build(ConfigurationProperty configurationProperty, final int level) {
        final SecurityFeatureOperator mainOperator = generateSecurityFeatureOperator(configurationProperty, level);
        if ((levels == 0) || (level < levels - 1)) {
            final SecurityFeatureOperatorBuilder builder = this;
            return new AbstractDependencyOperatorBuilder(configurationProperty, getObligationField(level)) {

                @Override
                protected SecurityFeatureOperator generateMainOperator(ConfigurationProperty mainProperty) {
                    return mainOperator;
                }

                @Override
                protected SecurityFeatureOperator generateDependentOperator(String mainValue, ConfigurationProperty dependentProperty) {
                    return builder.build(dependentProperty, level + 1);
                }
            }.build();
        } else {
            return mainOperator;
        }
    }

    private OperatorType[] getOperatorTypes(int level) {
        int length = operatorTypes.length;
        if (length > level) {
            return operatorTypes[level];
        } else {
            return operatorTypes[length - 1];
        }
    }

    private ObligationField getObligationField(int level) {
        int length = obligationFields.length;
        if (length > level) {
            return obligationFields[level];
        } else {
            return obligationFields[length - 1];
        }
    }

    private SecurityFeatureOperator generateSecurityFeatureOperator(ConfigurationProperty configurationProperty, int level) {
        OperatorType[] types = getOperatorTypes(level);
        if (types.length == 1) {
            return generateSecurityFeatureOperator(configurationProperty, level, types[0]);
        } else {
            MultipleOperator operator = new MultipleOperator();
            for (OperatorType type : types) {
                operator.add(generateSecurityFeatureOperator(configurationProperty, level, type));
            }
            return operator;
        }
    }

    private SecurityFeatureOperator generateSecurityFeatureOperator(ConfigurationProperty configurationProperty, int level, OperatorType type) {
        switch (type) {
            case FUNCTIONALITY:
                return new FunctionalityOperator(configurationProperty.getDefaultValue(), configurationProperty.getValues(), getObligationField(level));
            case PARAMETER:
                if ((parameterNames.length <= level) || (parameterNames[level] == null)) {
                    throw new IllegalStateException("No parameter name specified for level: " + level);
                }
                return new MappedParameterOperator(parameterNames[level], configurationProperty.getDefaultValue(), configurationProperty.getValues(), getObligationField(level));
            case PARAMETER_WITH_SHORT_VALUES:
                if ((parameterNames.length <= level) || (parameterNames[level] == null)) {
                    throw new IllegalStateException("No parameter name specified for level: " + level);
                }
                return new MappedParameterOperator(parameterNames[level], configurationProperty.getDefaultShortValue(), configurationProperty.getShortValues(), getObligationField(level));
            default:
                throw new IllegalArgumentException("Invalid operator type for level " + level + ": " + type);
        }
    }

    public static SecurityFeatureOperator newFunctionalityOperator(ConfigurationProperty configurationProperty, ObligationField obligationField) {
        return newFunctionalityOperator(configurationProperty, obligationField, 0);
    }

    public static SecurityFeatureOperator newFunctionalityOperator(ConfigurationProperty configurationProperty, ObligationField obligationField, int levels) {
        return new SecurityFeatureOperatorBuilder(configurationProperty).setLevels(levels).setObligationFields(obligationField).build();
    }

    public static SecurityFeatureOperator newParameterOperator(ConfigurationProperty configurationProperty, ObligationField obligationField, String... parameterNames) {
        return new SecurityFeatureOperatorBuilder(configurationProperty).setParameterNames(parameterNames).setObligationFields(obligationField).build();
    }

    public static SecurityFeatureOperator newParameterOperatorWithShortValues(ConfigurationProperty configurationProperty, ObligationField obligationField, String... parameterNames) {
        return new SecurityFeatureOperatorBuilder(configurationProperty).setOperatorTypes(OperatorType.PARAMETER_WITH_SHORT_VALUES).setParameterNames(parameterNames).setObligationFields(obligationField).build();
    }

}
