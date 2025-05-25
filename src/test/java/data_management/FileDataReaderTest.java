package data_management;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import static org.junit.jupiter.api.Assertions.*;

class FileDataReaderTest {

    @TempDir Path tempDir;
    private DataStorage storage;
    private FileDataReader reader;

    @BeforeEach
    void setup() {
        storage = DataStorage.getInstance();
        reader = new FileDataReader(tempDir.toString());
    }

    @Test
    void readDataParsesCsvAndStoresRecords() throws IOException {
        // create a CSV file "out.csv" with two lines
        Path file = tempDir.resolve("out.csv");
        String line1 = "5," + System.currentTimeMillis() + ",HeartRate,75.5";
        String line2 = "5," + (System.currentTimeMillis()+1000) + ",SpO2,95.2";
        Files.write(file, List.of(line1, line2));

        // run reader
        reader.readData(storage);

        // assert DataStorage now has a patient 5 with two records
        Patient p = storage.getAllPatients().stream()
                    .filter(x -> x.getPatientId() == 5).findFirst().orElse(null);
        assertNotNull(p, "Patient 5 should exist");
        List<PatientRecord> recs = p.getRecords(0, Long.MAX_VALUE);
        assertEquals(2, recs.size());
        // verify types
        assertTrue(recs.stream().anyMatch(r -> r.getRecordType().equals("HeartRate")));
        assertTrue(recs.stream().anyMatch(r -> r.getRecordType().equals("SpO2")));
    }
}
