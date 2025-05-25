package alerts;

import com.data_management.*;
import com.alerts.Alert;
import com.alerts.AlertGenerator;
import org.junit.jupiter.api.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    private DataStorage storage;
    private List<Alert> captured;
    private AlertGenerator generator;

    @BeforeEach
    void setup() {
        storage = new DataStorage();
        captured = new ArrayList<>();
        // subclass to capture triggerAlert invocations
        generator = new AlertGenerator(storage) {
            @Override
            protected void triggerAlert(Alert alert) {
                captured.add(alert);
            }
        };
    }

    @Test
    void bpIncreasingTrendTriggersOneAlert() {
        int pid = 1;
        long t = 1_000L;
        storage.addPatientData(pid, 120, "BloodPressure", t);
        storage.addPatientData(pid, 131, "BloodPressure", t + 1_000);
        storage.addPatientData(pid, 142, "BloodPressure", t + 2_000);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);
        assertFalse(captured.isEmpty(), "Should trigger at least one BP trend alert");
        assertTrue(captured.stream().anyMatch(a -> a.getCondition().contains("BP trend")));
    }

    @Test
    void spo2LowTriggersAlert() {
        int pid = 2;
        long t = System.currentTimeMillis();
        storage.addPatientData(pid, 90, "SpO2", t);
        Patient p = storage.getAllPatients().get(0);

        generator.evaluateData(p);
        assertEquals(1, captured.size());
        assertEquals("SpO2 low", captured.get(0).getCondition());
    }

    @Test
    void hypotensiveHypoxemiaTriggersCombinedAlert() {
        int pid = 3;
        long t = System.currentTimeMillis();
        storage.addPatientData(pid, 85, "BloodPressure", t);
        storage.addPatientData(pid, 88, "BloodPressure", t + 1000);
        storage.addPatientData(pid, 90, "BloodPressure", t + 2000);
        storage.addPatientData(pid, 91, "SpO2", t);
        storage.addPatientData(pid, 89, "SpO2", t + 1000);
        Patient p = storage.getAllPatients().get(0);

        generator.evaluateData(p);
        assertTrue(captured.stream()
                .anyMatch(a -> a.getCondition().equals("Hypotensive hypoxemia")));
    }
}
