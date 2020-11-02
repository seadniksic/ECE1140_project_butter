package sample;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;


public class Test_Prototype {


    @Test
    void correct2DDimensions() throws RemoteException, FileNotFoundException {
        Block oneBlock = new Block(null, 0, 0);
        assertEquals(oneBlock.coords[0], 0);
    }

}
