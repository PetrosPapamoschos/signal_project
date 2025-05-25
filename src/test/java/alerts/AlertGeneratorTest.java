package alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.alerts.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    private DataStorage storage;
    private List<Alert> captured;
    private AlertGenerator generator;

    @BeforeEach
    void setup() {
        storage = DataStorage.getInstance();
        storage.clearAllData();             // make sure you added this helper

        captured = new ArrayList<>();
        generator = new AlertGenerator() {
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

        assertEquals(1, captured.size(), "Expected exactly one BP alert");
        String msg = captured.get(0).getCondition();
        assertTrue(msg.contains("increasing trend"),
                   "Expected 'increasing trend' in \"" + msg + "\"");
    }

    @Test
    void spo2CriticallyLowTriggersAlert() {
        int pid = 2;
        long t = System.currentTimeMillis();
        storage.addPatientData(pid, 90, "SpO2", t);

        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        assertEquals(1, captured.size());
        String msg = captured.get(0).getCondition();
        assertTrue(msg.contains("critically low"),
                   "Expected 'critically low' in \"" + msg + "\"");
    }

    @Test
    void hypotensiveHypoxemiaTriggersCombinedAlert() {
        int pid = 3;
        long t = System.currentTimeMillis();
        // BP below 90
        storage.addPatientData(pid, 85, "BloodPressure", t);
        storage.addPatientData(pid, 88, "BloodPressure", t + 1_000);
        storage.addPatientData(pid, 90, "BloodPressure", t + 2_000);
        // SpO2 below 92
        storage.addPatientData(pid, 91, "SpO2", t);
        storage.addPatientData(pid, 89, "SpO2", t + 1_000);

        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        assertTrue(captured.stream()
            .anyMatch(a -> a.getCondition().toLowerCase()
                .contains("hypotensive hypoxemia")),
            "Expected a 'hypotensive hypoxemia' alert");
    }
}
