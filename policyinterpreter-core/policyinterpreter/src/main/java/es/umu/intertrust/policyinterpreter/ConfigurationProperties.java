package es.umu.intertrust.policyinterpreter;

import java.util.Properties;

/**
 *
 * @author Juanma
 */
public enum ConfigurationProperties {

    POLICY_INTERPRETER_CONF("policyinterpreter.conf", "."),
    POLICY_INTERPRETER_CONF_FILENAME("policyinterpreter.conf.filename", "policyinterpreter.xml"),
    POLICY_INTERPRETER_HANDLERS("policyinterpreter.handlers"),
    POLICY_INTERPRETER_HANDLERS_CONF("policyinterpreter.handlers.conf"),
    POLICY_MANAGER_WARN_UNRELATED_RULE_DEACTIVATIONS("policymanager.warnUnrelatedRuleDeactivations", "true");
    String propertyName;
    String defaultValue;

    private ConfigurationProperties(String propertyName) {
        this.propertyName = propertyName;
    }

    private ConfigurationProperties(String propertyName, String defaultValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public String getString(Properties properties) {
        if (defaultValue == null) {
            return properties.getProperty(propertyName);
        } else {
            return properties.getProperty(propertyName, defaultValue);
        }
    }

    public Boolean getBoolean(Properties properties) {
        String string = getString(properties);
        if (string == null) {
            return null;
        } else {
            return Boolean.parseBoolean(string);
        }
    }

}
