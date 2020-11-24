package resources;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.rmi.RemoteException;

public class test {

    @BeforeEach
    void setUp() throws RemoteException {
        Train_Model_Catalogue.create_Model(3);

    }

    @Test
    void train_Is_Spawned() throws RemoteException {
        Train_Model_Catalogue.create_Model(5);
        assertEquals(3, Train_Model_Catalogue.trains.get(0).num_Cars);
        assertEquals(5, Train_Model_Catalogue.trains.get(1).num_Cars);
    }

    @Test
    void set_Advertisement() {
        Train_Model_Catalogue.trains.get(0).set_Advertisements(true);
        assertTrue(Train_Model_Catalogue.trains.get(0).get_Advertisements());
    }

    @Test
    void set_Door_Status() {
        Train_Model_Catalogue.trains.get(0).set_Left_Door_Status(true);
        assertTrue(Train_Model_Catalogue.trains.get(0).get_Left_Door_Status());
    }

}
