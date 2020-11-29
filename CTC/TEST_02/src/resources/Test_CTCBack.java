/*

Zachary Turner
Group 2
Tests for schedule class
*/

package resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Test_CTCBack {
    //create variable with scope for all tests
    CTC_Back tester;

    //define the variable before each @Test
    @BeforeEach
    void setUp() throws Exception {
        tester = new CTC_Back();
    }

    //Test Default constructor for schedule class
    //lists are created empty
    //test the lists are of size zero
    @Test
    void test_Schedule_Create(){
        assertEquals(0,tester.get_Train_List().size());
        assertEquals(0,tester.get_Line_List().size());
    }
/*
    @Test
    void test_Dispatch2() throws FileNotFoundException , RemoteException {
        tester.import_Train_Schedule();
        LocalTime t = LocalTime.parse("00:40");
        tester.dispatch(0, "EDGEBROOK", t);

        System.out.println("Authority: " + tester.get_Train_List().get(0).get_Authority());
        System.out.println("Sug Speed: " + tester.get_Train_List().get(0).get_Suggest_Speed());
    }
    @Test
    void test_Dispatch1() throws FileNotFoundException , RemoteException {
        tester.import_Train_Schedule();
        tester.dispatch(0);
        System.out.println("Authority: " + tester.get_Train_List().get(0).get_Authority());
        System.out.println("Sug Speed: " + tester.get_Train_List().get(0).get_Suggest_Speed());
    }

 */
    //Test that the import schedule function works
    //function reads from file and files list
    //test that list is greater than zero
    @Test
    void test_Import_Schedule() throws FileNotFoundException {
        tester.import_Train_Schedule();
        int train_List_Length = tester.get_Train_List().size();
        for(int k = 0; k < train_List_Length;k++) {
            /*
            System.out.println(tester.get_Train_List().get(k).get_Name()+" : ");
            System.out.println("INFRASTRUCTURES: ");
            for (String s : tester.get_Train_List().get(k).get_Infrastructure_List()) {
                System.out.print(s + " , ");
            }
            System.out.println("");
            System.out.println("TIMES: ");
            for (LocalTime t : tester.get_Train_List().get(k).get_Time_List()) {
                System.out.print(t + " , ");
            }
            System.out.println("");

             */
            //assertTrue(tester.get_Train_List().get(k).get_Time_List().size() == tester.get_Train_List().get(k).get_Infrastructure_List().size());
        }
        assertTrue(tester.get_Line_List().size() == 1 );
        //assertTrue(tester.get_Line_List().get(0).get_Section_List().size() == 26);
        int zero = 0;
        assertTrue(train_List_Length > zero);
    }

    //Test that the import track function works
    //function reads from file and files list
    //test that list is greater than zero

    @Test
    void test_Determine_Authority() throws FileNotFoundException {

    }
}



/*
//////////////////////////INVOKES MY OWN SIM TIME///////////
        Task task = new Task<Void>() {
            @Override public Void call() {
                for(int i = 0; i <100; i++) {
                    System.out.println("HIT ++");
                    try {
                        Network.server_Object.update_Time(++simTime);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return null;
            }
        };
        new Thread(task).start();



 */
