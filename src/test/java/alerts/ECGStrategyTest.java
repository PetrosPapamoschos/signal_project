package alerts;

import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import com.alerts.*;

import static org.junit.jupiter.api.Assertions.*;

class ECGStrategyTest {

    @Test
    void ecgPeakAboveThresholdTriggers() {
        Patient p = new Patient(1);
        long base = System.currentTimeMillis();
        // baseline around 100
        for (int i = 0; i < 5; i++) {
            p.addRecord(100 + i, "ECG", base + i*100);
        }
        // add a big peak
        p.addRecord(200, "ECG", base + 600);

        ECGStrategy strat = new ECGStrategy();
        String type = strat.checkAndReturnType(p);
        assertEquals("ECG", type);
        assertTrue(strat.getCondition().contains("ECG peak abnormal"));
    }

    @Test
    void noAlertForFlatECG() {
        Patient p = new Patient(2);
        long base = System.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            p.addRecord(100, "ECG", base + i*100);
        }
        ECGStrategy strat = new ECGStrategy();
        assertNull(strat.checkAndReturnType(p));
    }
}
