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
        tester.set_Track_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\blue_track");
        tester.set_Schedule_Path("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\blue_schedule");

        tester.import_Train_Schedule();
        tester.import_Track_File();

        System.out.println("CALC AUTH HIT:");
        Integer blocks_between = 10;

        int lineIndex = -1;
        boolean open = true;
        //First check if path is open
        //assume it is open for now

        int j = 0;

        while(j < 10){




            for (int i = 0; i < tester.get_Line_List().size(); i++) {
                if (tester.get_Line_List().get(i).get_Line().equals(tester.get_Train_List().get(0).get_Current_Line())) {
                    lineIndex = i;
                    break;
                }
            }



            System.out.println("Line Index: " + lineIndex);
            //check line occupancy after current
            int nextBlock = tester.get_Train_List().get(0).get_Current_Block() + 1;
            System.out.println(" ----------------");

            for (int i = 1; i <= 5; i++) {

                //if(lineList.get(lineIndex).get_Block_Occupancy(nextBlock + i) == true){
                // trainList.get(trainIndex).set_Authority(0);
                // break;
                // }else

                String infrastructureHolder = tester.get_Line_List().get(0).get_Block_Infrastructure(nextBlock + i );
                System.out.println("Station output " + infrastructureHolder);
                if (infrastructureHolder.contains("Station")) {
                    tester.get_Train_List().get(0).set_Authority(i);

                    break;
                } else {
                    tester.get_Train_List().get(0).set_Authority(i);
                }

            }

            if(tester.get_Line_List().get(0).get_Block_Infrastructure(nextBlock).contains("Station")){
                tester.get_Train_List().get(0).set_Authority(0);
            }

            System.out.println("Authority = " + tester.get_Train_List().get(0).get_Authority());
            tester.get_Train_List().get(0).set_Current_Block( tester.get_Train_List().get(0).get_Current_Block() + 1 );
            j++;
        }
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
