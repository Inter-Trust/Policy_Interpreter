package es.umu.intertrust.policyinterpreter;

import es.umu.intertrust.policyinterpreter.manager.InitializationException;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcer;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcingException;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.manager.InterpreterManager;
import es.umu.intertrust.policyinterpreter.manager.InterpreterManagerException;
import es.umu.intertrust.policyinterpreter.policy.PolicyManager;
import es.umu.intertrust.policyinterpreter.policy.PolicyNotificationHandler;
import es.umu.intertrust.policyinterpreter.policy.PolicyNotificationHandlerException;
import es.umu.intertrust.policyinterpreter.policyhandlers.QueryManager;
import es.umu.intertrust.policyinterpreter.query.DeploymentQueryManager;
import es.umu.intertrust.policyinterpreter.query.PolicyQueryManager;
import es.umu.intertrust.policyinterpreter.util.LoggingUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class PolicyInterpreter {

    public static final Logger logger = Logger.getLogger(PolicyInterpreter.class.getName());

    public InterpreterManager initializeManager() throws InitializationException {
        logger.log(Level.INFO, "Starting policy interpreter...");
        Configuration config = Configuration.getInstance();
        InterpreterManager manager = new InterpreterManager();
        PolicyManager policyManager = new PolicyManager(config.getPolicyProperties());
        DeploymentManager deploymentManager = new DeploymentManager(config.getDeploymentProperties());

        try {
            PolicyNotificationHandler notificationHandler = loadInstance(PolicyNotificationHandler.class, config.getPolicyNotificationHandlerClass());
            notificationHandler.initialize(config.getPolicyProperties());
            policyManager.setNotificationHandler(notificationHandler);
        } catch (PolicyNotificationHandlerException ex) {
            throw new InitializationException("Unable to initialize policy notification handler: " + ex.getMessage(), ex);
        }
        try {
            DeploymentEnforcer enforcer = loadInstance(DeploymentEnforcer.class, config.getDeploymentEnforcerClass());
            enforcer.initialize(deploymentManager);
            deploymentManager.setEnforcer(enforcer);
        } catch (DeploymentEnforcingException ex) {
            throw new InitializationException("Unable to initialize deployment enforcer: " + ex.getMessage(), ex);
        }

        List<QueryManager> queryManagers = new ArrayList<QueryManager>();
        List<PolicyQueryManager> policyQueryManagers = loadInstances(PolicyQueryManager.class, config.getPolicyQueryManagerClasses());
        for (PolicyQueryManager policyQueryManager : policyQueryManagers) {
            policyQueryManager.initialize(policyManager);
        }
        queryManagers.addAll(policyQueryManagers);

        List<DeploymentQueryManager> deploymentQueryManagers = loadInstances(DeploymentQueryManager.class, config.getDeploymentQueryManagerClasses());
        for (DeploymentQueryManager deploymentQueryManager : deploymentQueryManagers) {
            deploymentQueryManager.initialize(deploymentManager);
        }
        queryManagers.addAll(deploymentQueryManagers);

        manager.initialize(policyManager, deploymentManager, queryManagers);
        return manager;
    }

    public void runManager(final InterpreterManager interpreter) throws InterpreterManagerException {
        interpreter.start();
        logger.log(Level.INFO, "Policy interpreter started.");
        logger.log(Level.INFO, "Listening for policy changes...");
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    logger.log(Level.INFO, "Stopping policy interpreter...");
                    interpreter.stop();
                } catch (InterpreterManagerException ex) {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            LoggingUtils.readConfiguration();
            Configuration.load();
            PolicyInterpreter interpreter = new PolicyInterpreter();
            InterpreterManager manager = interpreter.initializeManager();
            interpreter.runManager(manager);
        } catch (ConfigurationParsingException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (InitializationException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (InterpreterManagerException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private static <T> List<T> loadInstances(Class<T> type, List<String> classNames) throws InitializationException {
        List<T> loadedInstances = new ArrayList<T>();
        for (String className : classNames) {
            loadedInstances.add(loadInstance(type, className));
        }
        return loadedInstances;
    }

    private static <T> T loadInstance(Class<T> type, String className) throws InitializationException {
        try {
            Class loadedClass = ClassLoader.getSystemClassLoader().loadClass(className);
            if (type.isAssignableFrom(loadedClass)) {
                return (T) loadedClass.newInstance();
            } else {
                throw new InitializationException("Invalid class: " + className + " cannot be cast to " + type.getName());
            }
        } catch (ClassNotFoundException ex) {
            throw new InitializationException("Class not found: " + className, ex);
        } catch (InstantiationException ex) {
            throw new InitializationException("Unable to instantiate class: " + className, ex);
        } catch (IllegalAccessException ex) {
            throw new InitializationException("Illegal access instantiating class: " + className, ex);
        }
    }

}
