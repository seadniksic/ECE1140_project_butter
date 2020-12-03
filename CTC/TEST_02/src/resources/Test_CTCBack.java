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
    void test_Updating_Authority() throws FileNotFoundException, RemoteException, InterruptedException {
        tester.set_Track_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\track_thursday");
        tester.set_Schedule_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\schedule_thursday");

        tester.import_Train_Schedule();
        tester.import_Track_File();

        tester.dispatch(0); // dispatch train 0

        System.out.println("------------------------------------------------------------------------------------------");
        tester.train_Moved(0,63);
        System.out.println("------------------------------------------------------------------------------------------");
        tester.train_Moved(0,64);
        System.out.println("------------------------------------------------------------------------------------------");
        tester.train_Moved(0,65);


    }




    @Test
    void test_Determine_Authority() throws FileNotFoundException, RemoteException, InterruptedException {
        tester.set_Track_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\track_thursday");
        tester.set_Schedule_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\schedule_thursday");

        tester.import_Train_Schedule();
        tester.import_Track_File();


        tester.calculate_Authority(0);

        for(int i = 62; i <= 65; i++)
        tester.train_Moved(0,i);




    }

    @Test
    void test_Suggested_Speed() throws FileNotFoundException{
        tester.set_Track_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\track_thursday");
        tester.set_Schedule_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\schedule_thursday");

        tester.import_Train_Schedule();
        tester.import_Track_File();


        tester.calculate_Suggested_Speed(0);
        System.out.println(tester.get_Train_List().get(0).get_Suggest_Speed());
    }

    @Test
    void test_Determine_Path() throws FileNotFoundException {
        tester.set_Track_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\track_thursday");
        tester.set_Schedule_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\schedule_thursday");

        tester.import_Train_Schedule();
        tester.import_Track_File();

        System.out.println("Number of Lines = " +tester.get_Line_List().size());



        System.out.println(" ");
        System.out.println("__________________________");
        System.out.println("Poplar to shannon");
        tester.get_Train_List().get(0).set_Current_Index(4);
        tester.calculate_Authority(0);
        //tester.get_Line_List().get(0).get_Path(89, 96);//this is wrong if you go 88 to 96 poplar to shannon


        System.out.println(" ");
        System.out.println("__________________________");
        System.out.println("shannon to dormont");
        tester.get_Train_List().get(0).set_Current_Index(5);
        tester.calculate_Authority(0);
        //tester.get_Line_List().get(0).get_Path(97, 105);//this works Castle shannon to dormont




        //tester.get_Line_List().get(0).get_Path(106, 114);//this works  dormont to glen
        //tester.get_Line_List().get(0).get_Path(142, 22);//this works central to whited

        System.out.println(" ");
        System.out.println("__________________________");
        System.out.println("Whited to STATION");
        tester.get_Line_List().get(0).get_Path(22,16);

        System.out.println(" ");
        System.out.println("__________________________");
        System.out.println("STATION to EDGEBROOK");
        tester.get_Line_List().get(0).get_Path(16,9);

        System.out.println(" ");
        System.out.println("__________________________");
        System.out.println("EDGEBROOK to PIONEER");
        tester.get_Line_List().get(0).get_Path(8, 2);


        System.out.println(" ");
        System.out.println("__________________________");
        System.out.println("PIONEER to STATION");
        tester.get_Line_List().get(0).get_Path(1, 9);


    }
}



/*
//////////////////////////INVOKES MY OWN SIM TIME///////////
          Task task = new Task<Void>() {
                @Override public Void call() throws InterruptedException {
                    for(int i = 0; i <100; i++) {
                        System.out.println("HIT ++");
                        try {
                            int simTime = 0;
                            Network.server_Object.update_Time(++simTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Thread.sleep(500);
                    }
                    return null;
                }
            };
            new Thread(task).start();


 */
