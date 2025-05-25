package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;


    public class ECGStrategy implements AlertStrategy {
    private static final int WINDOW = 5;
    private static final double FACTOR = 1.5;

    private String condition;

    @Override
    public String checkAndReturnType(Patient p) {
        long now = System.currentTimeMillis();
        List<PatientRecord> ecg = p.getRecords(now - 60*60_000L, now);
        if (ecg.size() <= WINDOW) return null;

        ecg.sort(Comparator.comparingLong(r -> r.getTimestamp()));
        Deque<Double> window = new ArrayDeque<>();
        double sum = 0;
        for (int i = 0; i < WINDOW; i++) {
            double v = ecg.get(i).getMeasurementValue();
            window.addLast(v);
            sum += v;
        }
        for (int i = WINDOW; i < ecg.size(); i++) {
            double curr = ecg.get(i).getMeasurementValue();
            double avg  = sum / WINDOW;
            if (curr > avg * FACTOR) {
                condition = String.format(
                  "ECG peak abnormal: value=%.1f (avg=%.1f)", curr, avg);
                return "ECG";
            }
            sum += curr - window.removeFirst();
            window.addLast(curr);
        }
        return null;
    }

    public String getCondition() { return condition; }
}
