package com.alerts;

import java.util.List;
import java.util.Map;
import com.data_management.DataStorage;
import com.data_management.Patient;

/**
 * The {@code AlertGenerator} class is responsible for evaluating patient data
 * and triggering alerts based on predefined conditions. It uses a set of
 * strategies to analyze patient data and generates alerts when conditions are met.
 *
 * <p>This class works with {@link AlertManager} to manage and dispatch alerts
 * and uses {@link AlertFactory} to create specific types of alerts.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     AlertGenerator generator = new AlertGenerator();
 *     generator.evaluateData(patient);
 * </pre>
 *
 * @see AlertManager
 * @see AlertFactory
 * @see Alert
 */
public class AlertGenerator {

    private static final AlertManager manager = new AlertManager();

    private final Map<String, AlertFactory> factories = Map.of(
        "BloodPressure", new BloodPressureAlertFactory(),
        "SpO2",           new BloodOxygenAlertFactory(),
        "ECG",            new ECGAlertFactory()
    );

    private final List<AlertStrategy> strategies = List.of(
        new BloodPressureStrategy(),
        new HeartRateStrategy(),
        new OxygenSaturationStrategy(),
        new ECGStrategy()
    );

    /**
     * No‐arg constructor for normal use.
     */
    public AlertGenerator() {
    }

    /**
     * Backwards‐compatible constructor so old call sites/tests that
     * pass a DataStorage still compile. We ignore the passed‐in storage,
     * because this class retrieves it via DataStorage.getInstance().
     */
    public AlertGenerator(DataStorage storage) {
        this();
    }

    /**
     * Evaluates the data of a given patient using the defined strategies.
     * If any strategy identifies a condition that requires an alert,
     * an alert is triggered.
     *
     * @param p the {@link Patient} whose data is being evaluated
     */
    public void evaluateData(Patient p) {
        String pid = Integer.toString(p.getPatientId());
        long now = System.currentTimeMillis();

        for (AlertStrategy strat : strategies) {
            String type = strat.checkAndReturnType(p);
            if (type != null) {
                // each strategy should expose a getCondition()
                String cond = null;
                if (strat instanceof BloodPressureStrategy) {
                    cond = ((BloodPressureStrategy) strat).getCondition();
                } else if (strat instanceof HeartRateStrategy) {
                    cond = ((HeartRateStrategy) strat).getCondition();
                } else if (strat instanceof OxygenSaturationStrategy) {
                    cond = ((OxygenSaturationStrategy) strat).getCondition();
                } else if (strat instanceof ECGStrategy) {
                    cond = ((ECGStrategy) strat).getCondition();
                } else {
                    cond = "Condition: " + type;
                }
                triggerAlert(type, pid, cond, now);
            }
        }
    }

    /**
     * Factory‐based helper that creates an Alert and delegates to
     * {@link #triggerAlert(Alert)}.
     */
    protected void triggerAlert(String type, String pid, String cond, long ts) {
        AlertFactory factory = factories.get(type);
        Alert raw = factory.createAlert(pid, cond, ts);
        triggerAlert(raw);
    }

    /**
     * Core hook: dispatches an Alert via the AlertManager.
     * Tests override this method to capture alerts.
     *
     * @param alert the {@link Alert} to be triggered
     */
    protected void triggerAlert(Alert alert) {
        manager.dispatchAlert(alert);
    }
}
