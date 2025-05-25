package alerts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.alerts.*;
import com.alerts.PriorityAlertDecorator;

class PriorityAlertDecoratorTest {

    @Test
    void priorityIsWrappedAndPreserved() {
        Alert base = new BloodPressureAlert("1", "msg", 1234L);
        PriorityAlertDecorator decorated = new PriorityAlertDecorator(base, 7);

        assertEquals(7, decorated.getPriority());
        assertEquals("1", decorated.getPatientId());
        assertEquals("msg", decorated.getCondition());
        assertEquals(1234L, decorated.getTimestamp());
    }
}
