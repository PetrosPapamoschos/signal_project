package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {
    // Changed variable name to camelCase.
    private String baseDirectory;
    // Changed variable name to camelCase.
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();
    
    public FileOutputStrategy(String baseDirectory) {
        //corected baseDirectory variable
        this.baseDirectory = baseDirectory;
    }

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
                 //corected baseDirectory variable
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
             //corected baseDirectory variable
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}