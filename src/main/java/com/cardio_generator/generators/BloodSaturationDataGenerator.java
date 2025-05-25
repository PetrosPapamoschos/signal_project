package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code BloodSaturationDataGenerator} class simulates blood oxygen saturation data for patients.
 * <p>
 * This class maintains an array of the latest saturation values for each patient, and on each generation,
 * it applies a small random variation to simulate real-time fluctuations. The resulting value is clamped
 * within a realistic range (90% to 100%).
 * </p>
 *
 * <p>Example usage:
 * <pre>
 *     PatientDataGenerator generator = new BloodSaturationDataGenerator(100);
 *     generator.generate(1, outputStrategy);
 * </pre>
 * </p>
 *
 * @see com.cardio_generator.outputs.OutputStrategy
 * @see com.cardio_generator.generators.PatientDataGenerator
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Constructs a new {@code BloodSaturationDataGenerator} for the specified number of patients.
     * Initializes a baseline saturation value for each patient between 95% and 100%.
     *
     * @param patientCount the total number of patients for which simulation data will be generated.
     */
    public BloodSaturationDataGenerator(int patientCount) {
        // Create an array using patientCount + 1 to use 1-based indexing for patient IDs
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates simulated blood saturation data for the specified patient and outputs it using the provided strategy.
     * <p>
     * The method adds a small random variation (-1, 0, or +1) to the last known saturation value,
     * clamps the result between 90% and 100%, and updates the internal state.
     * </p>
     *
     * @param patientId      the unique identifier for the patient.
     * @param outputStrategy the strategy used to output the generated data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;

            // Output the new saturation value, appending a "%" sign.
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
