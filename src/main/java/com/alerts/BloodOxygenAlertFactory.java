package com.alerts;

public class BloodOxygenAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String pid, String cond, long ts) {
        return new BloodOxygenAlert(pid, cond, ts);
    }
}
