package com.alerts;
import java.util.*;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy {
    private static final long WINDOW_MS = 60*60_000L;
    private static final double LOW  = 50.0;
    private static final double HIGH = 100.0;

    private String condition;

    @Override
    public String checkAndReturnType(Patient p) {
        long now = System.currentTimeMillis();
        List<PatientRecord> hr =  new ArrayList<>(p.getRecords(now - WINDOW_MS, now));
        if (hr.isEmpty()) return null;

        PatientRecord latest = hr.stream()
                                 .max(Comparator.comparingLong(r -> r.getTimestamp()))
                                 .get();
        double v = latest.getMeasurementValue();
        if (v < LOW) {
            condition = String.format("Heart rate critically low: %.1f bpm", v);
            return "HeartRate";
        }
        if (v > HIGH) {
            condition = String.format("Heart rate critically high: %.1f bpm", v);
            return "HeartRate";
        }
        return null;
    }

    public String getCondition() { return condition; }
}
