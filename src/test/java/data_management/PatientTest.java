package data_management;

import org.junit.jupiter.api.*;
import java.util.List;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient patient;
    private final long BASE = 1_000L;

    @BeforeEach
    void setup() {
        patient = new Patient(42);
        // add three records at different times
        patient.addRecord(60, "HeartRate", BASE);
        patient.addRecord(65, "HeartRate", BASE + 10_000);
        patient.addRecord(70, "HeartRate", BASE + 20_000);
    }

    @Test
    void getRecordsFiltersByTimeInclusive() {
        // should include the second reading only
        List<PatientRecord> recs = patient.getRecords(BASE + 5_000, BASE + 15_000);
        assertEquals(1, recs.size());
        assertEquals(65, recs.get(0).getMeasurementValue());
    }

    @Test
    void getRecordsEmptyWhenNoMatch() {
        List<PatientRecord> recs = patient.getRecords(BASE + 100_000, BASE + 200_000);
        assertTrue(recs.isEmpty());
    }

    @Test
    void getPatientIdReturnsConstructorValue() {
        assertEquals(42, patient.getPatientId());
    }
}
