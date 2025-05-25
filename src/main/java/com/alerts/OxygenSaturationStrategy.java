package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.*;

public class OxygenSaturationStrategy implements AlertStrategy {
    private static final long WINDOW_MS = 10*60_000L;
    private static final double LOW_THRESHOLD = 92.0;
    private static final double DROP_DELTA   = 5.0;

    private String condition;

    @Override
    public String checkAndReturnType(Patient p) {
        long now = System.currentTimeMillis();
        List<PatientRecord> spo2 =  new ArrayList<>(p.getRecords(now - WINDOW_MS, now));
        if (spo2.isEmpty()) return null;

        spo2.sort(Comparator.comparingLong(r -> r.getTimestamp()));
        double latest = spo2.get(spo2.size() - 1).getMeasurementValue();
        if (latest < LOW_THRESHOLD) {
            condition = String.format("SpO2 critically low: %.1f%%", latest);
            return "SpO2";
        }
        long cutoff = spo2.get(spo2.size() - 1).getTimestamp() - WINDOW_MS;
        for (PatientRecord prev : spo2) {
            if (prev.getTimestamp() >= cutoff) {
                double delta = prev.getMeasurementValue() - latest;
                if (delta >= DROP_DELTA) {
                    condition = String.format(
                      "SpO2 rapid drop: from %.1f%% to %.1f%%", 
                      prev.getMeasurementValue(), latest);
                    return "SpO2";
                }
            }
        }
        return null;
    }

    public String getCondition() { return condition; }
}
