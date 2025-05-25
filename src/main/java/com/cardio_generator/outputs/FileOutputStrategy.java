package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code FileOutputStrategy} class implements the {@code OutputStrategy} interface
 * and provides functionality for writing health data output to files.
 * <p>
 * This strategy creates or appends to text files in a specified base directory.
 * For each label, it stores or retrieves the corresponding file path using an internal map.
 * </p>
 *
 * <p>Example usage:
 * <pre>
 *   OutputStrategy outputStrategy = new FileOutputStrategy("/data/output");
 *   outputStrategy.output(123, System.currentTimeMillis(), "HeartRate", "72 bpm");
 * </pre>
 * </p>
 *
 * @author 
 */

public class FileOutputStrategy implements OutputStrategy {
    // Changed variable name to camelCase.
    private String baseDirectory;
    // Changed variable name to camelCase.
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@code FileOutputStrategy} with the specified base directory.
     * The base directory is used as the root for all output files.
     *
     * @param baseDirectory the base directory for storing output files
     */
    public FileOutputStrategy(String baseDirectory) {
        //corected baseDirectory variable
        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes the specified data to a file determined by the label.
     * <p>
     * This method creates the base directory if it does not exist, determines the file path
     * based on the label (caching it for performance), and writes a formatted line containing
     * the patient id, timestamp, label, and data to the file. The file is opened in append mode.
     * </p>
     *
     * @param patientId the ID of the patient whose data is being written
     * @param timestamp the timestamp (in milliseconds since UNIX epoch) representing when the data was recorded
     * @param label     the label or type of the data (e.g., "HeartRate", "BloodPressure")
     * @param data      the data to be written to the file
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            //corected baseDirectory variable
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the filePath variable
        //corected fileMapc and baseDirectory variable and changed variable name to camelCase.
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                 //corected filePath variable
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
             //corected filePath variable
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}