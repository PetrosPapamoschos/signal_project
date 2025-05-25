package alerts;

import com.data_management.Patient;
import com.alerts.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BloodPressureStrategyTest {

    @Test
    void risingTrendTriggers() {
        long now = System.currentTimeMillis();
        Patient p = new Patient(1);
        p.addRecord(100, "BloodPressure", now - 2000);
        p.addRecord(115, "BloodPressure", now - 1000);
        p.addRecord(130, "BloodPressure", now);
        BloodPressureStrategy strat = new BloodPressureStrategy();

        String type = strat.checkAndReturnType(p);
        assertEquals("BloodPressure", type);
        String cond = strat.getCondition();
        assertTrue(cond.contains("increasing trend"));
    }

    @Test
    void criticalHighTriggers() {
        long now = System.currentTimeMillis();
        Patient p = new Patient(2);
        p.addRecord(100, "BloodPressure", now);
        p.addRecord(185, "BloodPressure", now + 500);
        p.addRecord(185, "BloodPressure", now + 1000);
        BloodPressureStrategy strat = new BloodPressureStrategy();

        String type = strat.checkAndReturnType(p);
        assertEquals("BloodPressure", type);
        assertTrue(strat.getCondition().contains("critically high"));
    }

    @Test
    void noAlertForNormalValues() {
        long now = System.currentTimeMillis();
        Patient p = new Patient(3);
        p.addRecord(110, "BloodPressure", now - 1000);
        p.addRecord(112, "BloodPressure", now);
        p.addRecord(111, "BloodPressure", now + 1000);
        BloodPressureStrategy strat = new BloodPressureStrategy();

        assertNull(strat.checkAndReturnType(p));
    }
}
