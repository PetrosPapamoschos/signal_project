package com.alerts;

/**
 * Factory Method interface for creating different Alert subtypes.
 */
public interface AlertFactory {
    Alert createAlert(String patientId, String condition, long timestamp);
}
