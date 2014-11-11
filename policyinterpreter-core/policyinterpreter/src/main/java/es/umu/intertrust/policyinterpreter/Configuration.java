package es.umu.intertrust.policyinterpreter;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Juanma
 */
public class Configuration {

    File policyHandlersDirectory;
    File policyHandlersConfLocation;
    String policyNotificationHandlerClass;
    String deploymentEnforcerClass;
    List<String> policyQueryManagerClasses;
    List<String> deploymentQueryManagerClasses;
    List<String> preconditionAvailableClasses;
    Properties policyProperties;
    Properties deploymentProperties;

    private static Configuration instance;

    private Configuration() {
    }

    public static Configuration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Configuration not loaded");
        }
        return instance;
    }

    public File getPolicyHandlersDirectory() {
        return policyHandlersDirectory;
    }

    public File getPolicyHandlersConfLocation() {
        return policyHandlersConfLocation;
    }

    public String getPolicyNotificationHandlerClass() {
        return policyNotificationHandlerClass;
    }

    public String getDeploymentEnforcerClass() {
        return deploymentEnforcerClass;
    }

    public List<String> getPolicyQueryManagerClasses() {
        return policyQueryManagerClasses;
    }

    public List<String> getDeploymentQueryManagerClasses() {
        return deploymentQueryManagerClasses;
    }

    public List<String> getPreconditionAvailableClasses() {
        return preconditionAvailableClasses;
    }

    public Properties getPolicyProperties() {
        return policyProperties;
    }

    public Properties getDeploymentProperties() {
        return deploymentProperties;
    }

    @Override
    public String toString() {
        return "PolicyInterpreterConfiguration {policyHandlersDirectory=" + policyHandlersDirectory + ", policyHandlersConfLocation=" + policyHandlersConfLocation + ", policyNotificationHandlerClass=" + policyNotificationHandlerClass + ", deploymentEnforcerClass=" + deploymentEnforcerClass + ", policyQueryManagerClasses=" + policyQueryManagerClasses + ", deploymentQueryManagerClasses=" + deploymentQueryManagerClasses + ", policyProperties=" + policyProperties + ", deploymentProperties=" + deploymentProperties + '}';
    }

    public static Configuration load() throws ConfigurationParsingException {
        File configurationDirectory = new File(ConfigurationProperties.POLICY_INTERPRETER_CONF.getString(System.getProperties()));
        if (!configurationDirectory.exists()) {
            throw new ConfigurationParsingException("Configuration directory not found: " + configurationDirectory.getAbsolutePath());
        }
        String configurationFilename = ConfigurationProperties.POLICY_INTERPRETER_CONF_FILENAME.getString(System.getProperties());
        File configurationFile = new File(configurationDirectory, configurationFilename);
        return loadFromFile(configurationFile);
    }

    public static Configuration loadFromFile(File configurationFile) throws ConfigurationParsingException {
        instance = new Configuration();
        new ConfigurationParser().parse(configurationFile, instance);
        String policyHandlersDirectory = ConfigurationProperties.POLICY_INTERPRETER_HANDLERS.getString(System.getProperties());
        if (policyHandlersDirectory != null) {
            instance.policyHandlersDirectory = new File(policyHandlersDirectory);
        }
        String policyHandlersConfLocation = ConfigurationProperties.POLICY_INTERPRETER_HANDLERS_CONF.getString(System.getProperties());
        if (policyHandlersConfLocation != null) {
            instance.policyHandlersConfLocation = new File(policyHandlersConfLocation);
        }
        if (instance.policyHandlersConfLocation == null) {
            instance.policyHandlersConfLocation = configurationFile.getParentFile();
        }
        return instance;
    }

}
