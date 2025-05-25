package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code PatientDataGenerator} interface defines a contract for generating patient
 * data for the health data simulation. Implementations of this interface are responsible
 * for simulating and outputting various types of patient data (e.g., ECG, blood pressure, etc.)
 * via the provided output strategy.
 *
 * <p>Example usage:
 * <pre>
 *     PatientDataGenerator generator = new ECGDataGenerator(100);
 *     generator.generate(1, outputStrategy);
 * </pre>
 * </p>
 *
 * @see com.cardio_generator.generators.ECGDataGenerator
 * @see com.cardio_generator.outputs.OutputStrategy
 */
public interface PatientDataGenerator {
     /**
     * Generates simulated data for the specified patient and outputs it using the provided strategy.
     *
     * @param patientId      the unique identifier for the patient whose data is being generated
     * @param outputStrategy the strategy used to output the generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
