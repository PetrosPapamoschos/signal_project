package alerts;

import org.junit.jupiter.api.Test;
import com.alerts.*;

import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    @Test
    void bloodPressureFactoryCreatesCorrectType() {
        AlertFactory f = new BloodPressureAlertFactory();
        Alert a = f.createAlert("42", "high bp", 1234L);
        assertTrue(a instanceof BloodPressureAlert);
        assertEquals("42", a.getPatientId());
        assertEquals("high bp", a.getCondition());
        assertEquals(1234L, a.getTimestamp());
    }

    @Test
    void bloodOxygenFactoryCreatesCorrectType() {
        AlertFactory f = new BloodOxygenAlertFactory();
        Alert a = f.createAlert("37", "low o2", 5678L);
        assertTrue(a instanceof BloodOxygenAlert);
        assertEquals("37", a.getPatientId());
        assertEquals("low o2", a.getCondition());
        assertEquals(5678L, a.getTimestamp());
    }

    @Test
    void ecgFactoryCreatesCorrectType() {
        AlertFactory f = new ECGAlertFactory();
        Alert a = f.createAlert("99", "ecg peak", 91011L);
        assertTrue(a instanceof ECGAlert);
        assertEquals("99", a.getPatientId());
        assertEquals("ecg peak", a.getCondition());
        assertEquals(91011L, a.getTimestamp());
    }
}
