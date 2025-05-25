package com.alerts;

import com.data_management.Patient;

public interface AlertStrategy {
    /**
     * @return the factory‐key ("BloodPressure", "SpO2", "ECG") if alert should fire,
     *         or null otherwise.
     */
    String checkAndReturnType(Patient patient);
}
