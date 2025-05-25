package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code AlertGenerator} class is responsible for simulating alert conditions for patients.
 * It implements the {@link com.cardio_generator.generators.PatientDataGenerator} interface and 
 * generates alert data with a random probability. The alert state for each patient is maintained 
 * internally. A resolved alert sends a "resolved" message, while a triggered alert sends a "triggered" message.
 *
 * <p>Example usage:
 * <pre>
 *     PatientDataGenerator generator = new AlertGenerator(100);
 *     generator.generate(1, outputStrategy);
 * </pre>
 * </p>
 *
 * @see com.cardio_generator.outputs.OutputStrategy
 * @see com.cardio_generator.generators.PatientDataGenerator
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    // Renamed AlertStates to alertStates to follow camelCase naming conventions.
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Constructs a new {@code AlertGenerator} for the specified number of patients.
     * Initializes the internal alert states array using 1-based indexing.
     *
     * @param patientCount the total number of patients for which alert simulation will be generated.
     */
    public AlertGenerator(int patientCount) {
        // Corrected alertStates variable name
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert data for the specified patient and outputs it using the provided strategy.
     * <p>
     * If the alert for the patient is already active, there is a 90% chance the alert will be resolved.
     * Otherwise, based on a defined probability, an alert is triggered.
     * </p>
     *
     * @param patientId      the unique identifier for the patient.
     * @param outputStrategy the strategy used to output the generated alert data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Corrected alertStates variable name
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    // Corrected alertStates variable name
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Renamed Lambda to lambda to follow lowercase naming for local variables.
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                // Corrected lambda variable name
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    // Corrected alertStates variable name
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
