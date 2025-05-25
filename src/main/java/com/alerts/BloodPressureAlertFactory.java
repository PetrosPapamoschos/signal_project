package com.alerts;

public class BloodPressureAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String pid, String cond, long ts) {
        return new BloodPressureAlert(pid, cond, ts);
    }
}