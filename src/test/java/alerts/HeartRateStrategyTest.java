package alerts;

import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import com.alerts.*;

import static org.junit.jupiter.api.Assertions.*;

class HeartRateStrategyTest {

    @Test
    void lowHeartRateTriggers() {
        Patient p = new Patient(1);
        long now = System.currentTimeMillis();
        p.addRecord(45, "HeartRate", now);
        HeartRateStrategy strat = new HeartRateStrategy();

        String type = strat.checkAndReturnType(p);
        assertEquals("HeartRate", type);
        assertTrue(strat.getCondition().contains("critically low"));
    }

    @Test
    void highHeartRateTriggers() {
        Patient p = new Patient(2);
        long now = System.currentTimeMillis();
        p.addRecord(105, "HeartRate", now);
        HeartRateStrategy strat = new HeartRateStrategy();

        String type = strat.checkAndReturnType(p);
        assertEquals("HeartRate", type);
        assertTrue(strat.getCondition().contains("critically high"));
    }

    @Test
    void normalHeartRateNoAlert() {
        Patient p = new Patient(3);
        long now = System.currentTimeMillis();
        p.addRecord(75, "HeartRate", now);
        HeartRateStrategy strat = new HeartRateStrategy();

        assertNull(strat.checkAndReturnType(p));
    }
}
