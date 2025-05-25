package com.data_management;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * Reads “*.csv” files produced by HealthDataSimulator (--output file:<dir>)
 * CSV format: patientId,timestamp,recordType,value
 */
public class FileDataReader implements DataReader {

    private final Path dir;

    public FileDataReader(String outputDir) {
        this.dir = Paths.get(outputDir);
    }

    @Override
    public void readData(DataStorage storage) throws IOException {
        try (Stream<Path> files = Files.list(dir).filter(p -> p.toString().endsWith(".csv"))) {
            files.forEach(path -> {
                try {
                    Files.lines(path).forEach(line -> parseLine(line, storage));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void parseLine(String csv, DataStorage storage) {
        String[] parts = csv.split(",");
        int patientId        = Integer.parseInt(parts[0]);
        long timestamp       = Long.parseLong(parts[1]);
        String recordType    = parts[2];
        double measurement   = Double.parseDouble(parts[3]);
        storage.addPatientData(patientId, measurement, recordType, timestamp);
    }
}
