package sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

public class TC_Test {
    TrackControllerCatalogue tester;
    OverallSoftware tester2;

    @BeforeEach
    void setUp() throws Exception {
        tester = new TrackControllerCatalogue();
    }

    //NOT USED ANYMORE
    /*@Test
    void test_Set_Speed_Authority() {
        tester.send_Speed_Authority(10,50,10);
        assertEquals(50, tester.ctcSpeed);
        assertEquals(10, tester.ctcAuthority);
    }

    @Test
    void test_Set_Speed_Authority_Nega() {
        tester.send_Speed_Authority(10,-50,-10);
        assertEquals(-1, tester.ctcSpeed);
        assertEquals(-1, tester.ctcAuthority);
    }

    @Test
    void test_Make_Controllers() throws RemoteException {
        String[] lines = new String[]{"red", "green"};
        int[][]blocks = new int[][] {{1,18,21,45,117,150,46,59,61,68,69,82,101,119,83,100}, {1, 2, 3}};
        tester.make_Controllers(lines, blocks);
        //assertEquals(6, tester.greenControllerNum);
    }*/

    @Test
    void test_Find_Controllers() throws RemoteException, FileNotFoundException {
        String[] lines = new String[]{"green", "red"};
        int[][]blocks = new int[][] {{1,20,21,45,117,150,46,60,61,68,69,82,101,119,83,100}, {1, 2, 3}};
        tester.make_Controllers(lines, blocks);
        assertEquals(2, tester.find_Controller("green", 24));
    }

    @Test
    void test_Train_Moved() throws RemoteException, FileNotFoundException, InterruptedException {
        String[] lines = new String[]{"green", "red"};
        int[][]blocks = new int[][] {{1,20,21,45,117,150,46,60,61,68,69,82,101,119,83,100}, {1, 2, 3}};
        tester.make_Controllers(lines, blocks);
        tester.train_Moved(0, 9);
    }
}
