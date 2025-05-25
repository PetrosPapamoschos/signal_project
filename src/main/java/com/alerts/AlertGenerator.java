package com.alerts;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private static final int   ECG_WINDOW_SIZE     = 5;      // # samples in sliding window
    private static final double ECG_THRESHOLD_FACTOR = 1.5;  // peak > avg × factor
    

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions are met.
     * If a condition is met, an alert is triggered via the {@link #triggerAlert(Alert)}
     * method. This method should define the specific conditions under which an alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {

    // collect recent records
    long now = System.currentTimeMillis();
    List<PatientRecord> lastHour = patient.getRecords(now - 3600_000L, now);

    // group by record type for convenience
    Map<String, List<PatientRecord>> byType = lastHour.stream()
            .collect(Collectors.groupingBy(PatientRecord::getRecordType));

    // Blood-pressure alerts 
    List<PatientRecord> bp = byType.getOrDefault("BloodPressure", List.of());
    checkBloodPressure(bp, patient.getPatientId());

    // Oxygen saturation alerts 
    List<PatientRecord> spo2 = byType.getOrDefault("SpO2", List.of());
    checkSpO2(spo2, patient.getPatientId());

    // Combined hypotensive-hypoxemia 
    checkCombined(bp, spo2, patient.getPatientId());

    //ECG peak detection 
    List<PatientRecord> ecg = byType.getOrDefault("ECG", List.of());
    checkECG(ecg, patient.getPatientId());
}

private void checkECG(List<PatientRecord> ecgRecords, int pid) {
        if (ecgRecords.size() <= ECG_WINDOW_SIZE) return;

        // Sort by timestamp ascending
        ecgRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        // Build a sliding window sum for fast average
        Deque<Double> window = new ArrayDeque<>(ECG_WINDOW_SIZE);
        double sum = 0;
        // initialize window with first N values
        for (int i = 0; i < ECG_WINDOW_SIZE; i++) {
            double v = ecgRecords.get(i).getMeasurementValue();
            window.addLast(v);
            sum += v;
        }

        // check each new sample against the average of the previous N
        for (int i = ECG_WINDOW_SIZE; i < ecgRecords.size(); i++) {
            double current = ecgRecords.get(i).getMeasurementValue();
            double avg = sum / ECG_WINDOW_SIZE;

            if (current > avg * ECG_THRESHOLD_FACTOR) {
                triggerAlert(new Alert(
                    Integer.toString(pid),
                    "ECG peak abnormal (value=" + current + ", avg=" + String.format("%.1f", avg) + ")",
                    System.currentTimeMillis()
                ));
            }

            // slide window forward
            double removed = window.removeFirst();
            window.addLast(current);
            sum += current - removed;
        }
    }

private void checkBloodPressure(List<PatientRecord> bp, int pid) {
    if (bp.size() < 3) return;

    // last three readings, newest last
    List<PatientRecord> last3 = bp.stream()
                                  .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                                  .skip(Math.max(0, bp.size() - 3))
                                  .toList();

    double v1 = last3.get(0).getMeasurementValue();
    double v2 = last3.get(1).getMeasurementValue();
    double v3 = last3.get(2).getMeasurementValue();

    // Trend ±10 mmHg
    if ((v2 - v1 > 10 && v3 - v2 > 10) || (v1 - v2 > 10 && v2 - v3 > 10)) {
        triggerAlert(new Alert(String.valueOf(pid), "BP trend", System.currentTimeMillis()));
    }

    // Critical thresholds (systolic/diastolic combined into one value for demo)
    if (v3 > 180 || v3 < 90) {
        triggerAlert(new Alert(String.valueOf(pid), "BP critical", System.currentTimeMillis()));
    }
}

private void checkSpO2(List<PatientRecord> spo2, int pid) {
    if (spo2.isEmpty()) return;

    PatientRecord latest = spo2.get(spo2.size() - 1);
    double value = latest.getMeasurementValue();
    if (value < 92) {
        triggerAlert(new Alert(String.valueOf(pid), "SpO2 low", System.currentTimeMillis()));
    }

    // rapid 5 % drop in ≤10 min
    long tenMinAgo = latest.getTimestamp() - 600_000L;
    spo2.stream()
        .filter(r -> r.getTimestamp() >= tenMinAgo && r.getTimestamp() < latest.getTimestamp())
        .findFirst()
        .ifPresent(prev -> {
            if (prev.getMeasurementValue() - value >= 5.0) {
                triggerAlert(new Alert(String.valueOf(pid), "SpO2 rapid drop", System.currentTimeMillis()));
            }
        });
}

private void checkCombined(List<PatientRecord> bp, List<PatientRecord> spo2, int pid) {
    if (bp.isEmpty() || spo2.isEmpty()) return;
    double lastBp = bp.get(bp.size() - 1).getMeasurementValue();
    double lastSp = spo2.get(spo2.size() - 1).getMeasurementValue();
    if (lastBp < 90 && lastSp < 92) {
        triggerAlert(new Alert(String.valueOf(pid), "Hypotensive hypoxemia", System.currentTimeMillis()));
    }
}



    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    protected void triggerAlert(Alert alert) {
    // For now it just prints, later AlertManager will handle the alert
    System.out.printf("ALERT! %s – %s%n", alert.getPatientId(), alert.getCondition());
}
}

