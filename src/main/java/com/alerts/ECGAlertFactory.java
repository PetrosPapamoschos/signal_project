package com.alerts;

public class ECGAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String pid, String cond, long ts) {
        return new ECGAlert(pid, cond, ts);
    }
}
