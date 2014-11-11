package es.umu.intertrust.policyinterpreter.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class PolicyChangesGatherer implements PolicyChangeListener, PolicyNotificationHandlerListener {

    boolean running;
    List<PolicyRuleChange> policyRuleChanges;
    NotificationThread notificationThread;
    List<PolicyChangeListener> listeners;

    public PolicyChangesGatherer(PolicyNotificationHandler notificationHandler) {
        this.running = false;
        this.policyRuleChanges = new ArrayList<PolicyRuleChange>();
        this.notificationThread = new NotificationThread();
        this.listeners = new ArrayList<PolicyChangeListener>();
        notificationHandler.addPolicyChangeListener(this);
        notificationHandler.addPolicyNotificationHandlerListener(this);
    }

    public synchronized void start() {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Starting policy changes gatherer.");
        this.running = true;
        this.notificationThread.start();
    }

    public synchronized void stop() {
        this.running = false;
        notify();
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public void notificationHandlerStarted() {
        start();
    }

    public void notificationHandlerStopped() {
        stop();
    }

    public synchronized void policyStatusChanged(List<PolicyRuleChange> policyRuleChanges) {
        if (!running) {
            IllegalStateException ex = new IllegalStateException("Policy changes gatherer is not running.");
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        }
        this.policyRuleChanges.addAll(policyRuleChanges);
        notify();
    }

    public synchronized boolean hasPolicyRuleChanges() {
        return !policyRuleChanges.isEmpty();
    }

    public synchronized List<PolicyRuleChange> drainPolicyRuleChanges() throws InterruptedException {
        if (policyRuleChanges.isEmpty()) {
            wait();
        }
        List<PolicyRuleChange> changes = new ArrayList<PolicyRuleChange>(policyRuleChanges);
        policyRuleChanges.clear();
        return changes;
    }

    public void addPolicyChangeListener(PolicyChangeListener listener) {
        listeners.add(listener);
    }

    public void removePolicyChangeListener(PolicyChangeListener listener) {
        listeners.remove(listener);
    }

    protected void firePolicyStatusChanged(List<PolicyRuleChange> policyRuleChanges) {
        for (PolicyChangeListener listener : listeners) {
            listener.policyStatusChanged(policyRuleChanges);
        }
    }

    private void stopGatherer() {
        stop();
    }

    class NotificationThread extends Thread {

        @Override
        public void run() {
            while (isRunning() || hasPolicyRuleChanges()) {
                try {
                    List<PolicyRuleChange> changes = drainPolicyRuleChanges();
                    if (!changes.isEmpty()) {
                        firePolicyStatusChanged(changes);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PolicyChangesGatherer.class.getName()).log(Level.SEVERE, "Notification thread interrupted: " + ex.getMessage(), ex);
                    stopGatherer();
                } catch (Throwable ex) {
                    Logger.getLogger(PolicyChangesGatherer.class.getName()).log(Level.SEVERE, "Unexpected error while processing policy: " + ex.getMessage(), ex);
                }
            }
        }
    }
}
