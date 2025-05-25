package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.*;

/**
 * Strategy for generating blood pressure alerts based on trend and critical thresholds.
 */
public class BloodPressureStrategy implements AlertStrategy {

    // 1-hour window in milliseconds
    private static final long WINDOW_MS = 60 * 60 * 1000L;
    private static final double TREND_DELTA      = 10.0;   // mmHg per reading
    private static final double HIGH_THRESHOLD   = 180.0;  // mmHg
    private static final double LOW_THRESHOLD    = 90.0;   // mmHg

    // holds the last condition that caused an alert
    private String condition;

    @Override
    public String checkAndReturnType(Patient p) {
        long now = System.currentTimeMillis();
        // grab all BP records in the last hour
        List<PatientRecord> bp =  new ArrayList<>(p.getRecords(now - WINDOW_MS, now));
        if (bp.size() < 3) {
            return null; 
        }

        // sort oldest→newest
        bp.sort(Comparator.comparingLong(PatientRecord::getTimestamp));
        int n = bp.size();
        double v1 = bp.get(n - 3).getMeasurementValue();
        double v2 = bp.get(n - 2).getMeasurementValue();
        double v3 = bp.get(n - 1).getMeasurementValue();

        // rising or falling trend of > TREND_DELTA over 3 readings
        if (v2 - v1 > TREND_DELTA && v3 - v2 > TREND_DELTA) {
            condition = String.format(
              "Blood pressure increasing trend: %.1f → %.1f → %.1f mmHg", 
               v1, v2, v3);
            return "BloodPressure";
        }
        if (v1 - v2 > TREND_DELTA && v2 - v3 > TREND_DELTA) {
            condition = String.format(
              "Blood pressure decreasing trend: %.1f → %.1f → %.1f mmHg", 
               v1, v2, v3);
            return "BloodPressure";
        }

        // critical threshold breach at the latest reading
        if (v3 > HIGH_THRESHOLD) {
            condition = String.format("Blood pressure critically high: %.1f mmHg", v3);
            return "BloodPressure";
        }
        if (v3 < LOW_THRESHOLD) {
            condition = String.format("Blood pressure critically low: %.1f mmHg", v3);
            return "BloodPressure";
        }

        // otherwise no alert
        return null;
    }

    /**
     * After checkAndReturnType(...) returns non-null,
     * call this to get the exact message.
     */
    public String getCondition() {
        return condition;
    }
}

