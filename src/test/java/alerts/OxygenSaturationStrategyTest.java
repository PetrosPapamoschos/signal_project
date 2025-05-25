package alerts;

import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import com.alerts.*;

import static org.junit.jupiter.api.Assertions.*;

class OxygenSaturationStrategyTest {

    @Test
    void criticallyLowTriggers() {
        Patient p = new Patient(1);
        long now = System.currentTimeMillis();
        p.addRecord(91, "SpO2", now);
        OxygenSaturationStrategy strat = new OxygenSaturationStrategy();

        String type = strat.checkAndReturnType(p);
        assertEquals("SpO2", type);
        assertTrue(strat.getCondition().contains("critically low"));
    }

    @Test
    void rapidDropTriggers() throws InterruptedException {
        Patient p = new Patient(2);
        long t1 = System.currentTimeMillis();
        p.addRecord(97, "SpO2", t1 - TimeUnit.MINUTES.toMillis(9));
        p.addRecord(91, "SpO2", t1);
        OxygenSaturationStrategy strat = new OxygenSaturationStrategy();

        String type = strat.checkAndReturnType(p);
        assertEquals("SpO2", type);
        assertTrue(strat.getCondition().contains("rapid drop"));
    }

    @Test
    void normalSaturationNoAlert() {
        Patient p = new Patient(3);
        p.addRecord(95, "SpO2", System.currentTimeMillis());
        OxygenSaturationStrategy strat = new OxygenSaturationStrategy();

        assertNull(strat.checkAndReturnType(p));
    }
}
