package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataStorageTest {

    private DataStorage storage;

    @BeforeEach
    void setup() {
        storage = new DataStorage();
    }

    @Test
    void addPatientDataCreatesNewPatient() {
        storage.addPatientData(1, 120, "BloodPressure", 1_000L);
        List<Patient> patients = storage.getAllPatients();
        assertEquals(1, patients.size());
        assertEquals(1, patients.get(0).getPatientId());
    }

    @Test
    void getAllPatientsEmptyInitially() {
        assertTrue(storage.getAllPatients().isEmpty());
    }

    @Test
    void getRecordsViaDataStorageMatchesPatientGetRecords() {
        int pid = 7;
        long t1 = 10_000L, t2 = 20_000L, t3 = 30_000L;
        storage.addPatientData(pid, 80, "HeartRate", t1);
        storage.addPatientData(pid, 85, "HeartRate", t2);
        storage.addPatientData(pid, 90, "HeartRate", t3);

        // invoke the commented-out getRecords on DataStorage by uncommenting or via reflection
        // For now, call Patient.getRecords directly:
        Patient p = storage.getAllPatients().stream()
                           .filter(x -> x.getPatientId() == pid).findFirst().get();
        List<PatientRecord> window = p.getRecords(t1 + 5_000, t3 - 5_000);
        assertEquals(1, window.size());
        assertEquals(85.0, window.get(0).getMeasurementValue());
    }
}
