package com.cardio_generator.outputs;

/**
 * The {@code OutputStrategy} interface defines a strategy for outputting health data.
 * Implementations of this interface provide a mechanism to output patient data, which
 * includes the patient's ID, a timestamp, a label (such as "HeartRate" or "BloodPressure"),
 * and the associated data.
 *
 * <p>Example usage:
 * <pre>
 *     OutputStrategy outputStrategy = new FileOutputStrategy("/data/output");
 *     outputStrategy.output(123, System.currentTimeMillis(), "HeartRate", "72 bpm");
 * </pre>
 * </p>
 *
 * @see FileOutputStrategy
 * @see TcpOutputStrategy
 * @see WebSocketOutputStrategy
 */
public interface OutputStrategy {
    /**
     * Outputs the specified patient data.
     *
     * @param patientId the unique identifier for the patient
     * @param timestamp the timestamp (in milliseconds since UNIX epoch) when the data was recorded
     * @param label     the label indicating the type of data (e.g., "HeartRate", "BloodPressure")
     * @param data      the data to output, typically represented as a String
     */
    void output(int patientId, long timestamp, String label, String data);
}
