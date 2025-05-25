package cardio_generator;

import org.junit.jupiter.api.Test;
import com.cardio_generator.HealthDataSimulator;

import static org.junit.jupiter.api.Assertions.*;

class HealthDataSimulatorSingletonTest {

    @Test
    void testSingletonInstance() {
        HealthDataSimulator s1 = HealthDataSimulator.getInstance();
        HealthDataSimulator s2 = HealthDataSimulator.getInstance();
        assertSame(s1, s2, "HealthDataSimulator.getInstance() should always return the same instance");
    }
}
