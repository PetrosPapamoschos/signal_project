package data_management;

import org.junit.jupiter.api.Test;
import com.data_management.DataStorage;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageSingletonTest {

    @Test
    void testSingletonInstance() {
        DataStorage ds1 = DataStorage.getInstance();
        DataStorage ds2 = DataStorage.getInstance();
        assertSame(ds1, ds2, "DataStorage.getInstance() should always return the same instance");
    }
}
