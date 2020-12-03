package sample;

import javafx.application.Platform;
import networking.Track_Controller_SW_Interface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackControllerCatalogue implements Track_Controller_SW_Interface {

    HashMap trackControllerMap;
    HashMap controllerBlocksMap;

    TrackController hardwareController = new TrackController();
    int hardwareNum = 0;

    public TrackControllerCatalogue() {
        trackControllerMap = new HashMap();
        controllerBlocksMap = new HashMap();
    }

    //TRACK MODEL
    public void make_Controllers(String[] lines, int[][] blocks) {
        //Green Line
        //c1 - 1-9, 12-10, 13-20
        //c2 - 29-21, 30-45, 150-117
        //c3 - 46-51, 52-57, 58-60
        //c4 - 62-61, 63-66, 67-69
        //c5 - 76-70, 77-82, 101-116
        //c6 - 85-83, 86-93, 100-94
        //Red Line
        //c1 - 1-9, 12-10, 13-20
        //c2 - 29-21, 30-45, 150-117
        //c3 - 46-51, 52-57, 58-60
        //c4 - 62-61, 63-66, 67-69
        //c5 - 76-70, 77-82, 101-116
        //c6 - 85-83, 86-93, 100-94'
        //c7 -
        for (int currentline = 0; currentline < lines.length; currentline++) {
            System.out.println(lines[currentline]);
            TrackController [] trackControllers = new TrackController[20];
            ArrayList<ArrayList> controllerBlocks = new ArrayList();

            for (int controller = 0; controller < blocks[currentline].length/6; controller++) {
                ArrayList controllerBlock = new ArrayList();

                for (int i = controller*6; i < controller*6 + 6; i+=2) {
                    if (blocks[currentline][i] < blocks[currentline][i+1]) {

                        for (int j = blocks[currentline][i]; j < blocks[currentline][i+1]+1; j++) {
                            controllerBlock.add(j);
                        }

                    }else if (blocks[currentline][i] > blocks[currentline][i+1]) {

                        for (int j = blocks[currentline][i+1]; j < blocks[currentline][i]+1; j++) {
                            controllerBlock.add(j);
                        }
                    }
                }

                trackControllers[controller] = new TrackController(controllerBlock);
                trackControllers[controller].set_Track_Line(lines[currentline].toLowerCase());
                controllerBlocks.add(controllerBlock);
                System.out.println(controllerBlock);
                controllerBlock = null;
            }

            controllerBlocksMap.put(lines[currentline].toLowerCase(), controllerBlocks);
            trackControllerMap.put(lines[currentline].toLowerCase(), trackControllers);
            trackControllers = null;
            controllerBlocks = null;
        }
    }

    public void train_Moved (int trainNum, int blockNum) throws RemoteException, FileNotFoundException, InterruptedException {
        System.out.println("Train " + trainNum + " moved to block " + blockNum);
        set_Block_State_Catalogue(blockNum);
        try {
            Network.c_Interface.train_Moved(trainNum, blockNum);
        } catch (NullPointerException | RemoteException e) {

        }

        /*if (find_Controller("green", blockNum) == hardwareNum) {
            Network.tchw_Interface.set_Block_State(blockNum, true);
            if (blockNum > hardwareController.get_Block_ID(0))
                Network.tchw_Interface.set_Block_State((blockNum-1), false);
        }*/
    }

    public void set_Broken_Rail(int lineIndex, int blockNum, boolean state) throws RemoteException {
        int controller = 0;

        if (lineIndex == 0) {
            controller = find_Controller("green", blockNum);
            ((TrackController[])trackControllerMap.get("green"))[controller].set_Broken_Rail(blockNum, state);
        } else if (lineIndex == 1) {
            controller = find_Controller("red", blockNum);
            ((TrackController[])trackControllerMap.get("red"))[controller].set_Broken_Rail(blockNum, state);
        }

        /*if (find_Controller("green", blockNum) == hardwareNum) {
            Network.tchw_Interface.set_Broken_Rail(0, blockNum, state);
        }*/
    }


    public void set_Track_Circuit_Failure(int lineIndex, int blockNum, boolean state) throws RemoteException {
        int controller = 0;

        if (lineIndex == 0) {
            controller = find_Controller("green", blockNum);
            ((TrackController[])trackControllerMap.get("green"))[controller].set_Track_Fail(blockNum, state);
        } else if (lineIndex == 1) {
            controller = find_Controller("red", blockNum);
            ((TrackController[])trackControllerMap.get("red"))[controller].set_Track_Fail(blockNum, state);
        }

        /*if (find_Controller("green", blockNum) == hardwareNum) {
            Network.tchw_Interface.set_Track_Circuit_Failure(0, blockNum, state);
        }*/
    }


    public void set_Power_Fail(int lineIndex, int blockNum, boolean state) throws RemoteException {
        int controller = 0;

        if (lineIndex == 0) {
            controller = find_Controller("green", blockNum);
            ((TrackController[])trackControllerMap.get("green"))[controller].set_Power_Fail(blockNum, state);
        } else if (lineIndex == 1) {
            controller = find_Controller("red", blockNum);
            ((TrackController[])trackControllerMap.get("red"))[controller].set_Power_Fail(blockNum, state);
        }

        /*if (find_Controller("green", blockNum) == hardwareNum) {
            Network.tchw_Interface.set_Power_Fail(0, blockNum, state);
        }*/
    }
    //TRACK MODEL

    //CTC
    public void create_Train(int cars, String line, int block) throws RemoteException {
        System.out.println("Create train with " + cars + " cars");
        System.out.println("on line " + line);
        System.out.println("to yard on " + block);
        Network.tm_Interface.spawn_Train_In_Yard(cars, 0, block);
    } // (CTC -> track controller ->) track model -> train model

    public void remove_Train(int trainNum) throws RemoteException {
        //Network.tm_Interface.send_Beacon_Information(train_Num);
    }

    public void send_Beacon_Information(int train_Num, String next_Stop) throws RemoteException {
        //Network.tm_Interface.send_Beacon_Information(train_Num, next_Stop);
    }

    public void send_Speed_Authority(int train_Num, double speed, int authority) throws RemoteException, InterruptedException {
        System.out.println("Speed and Authority");
        System.out.println("Train Number: " + train_Num);
        System.out.println("Speed: " + speed);
        System.out.println("Au: " + authority);

        Network.tm_Interface.send_Speed_Authority(train_Num, speed, authority);//call track models send method
    } // (CTC -> track controller ->) track model -> train model

    public boolean set_Switch_Manual(String trackLine, int blockNum) throws RemoteException {
        System.out.println("Line: " + trackLine + " to block " + blockNum);
        int controller = find_Controller(trackLine, blockNum);
        boolean currentState = ((TrackController[])trackControllerMap.get(trackLine.toLowerCase()))[controller].get_Switch_State();

        ((TrackController[])trackControllerMap.get(trackLine.toLowerCase()))[controller].set_Switch_State(!currentState);
        return currentState != ((TrackController[])trackControllerMap.get(trackLine.toLowerCase()))[controller].get_Switch_State();
    }

    public void add_Ticket(int trainNum, int numTickets) throws RemoteException {
        System.out.println("Train " + trainNum + " gets " + numTickets + " tickets");

        try {
            Network.c_Interface.add_Tickets(trainNum, numTickets);
        } catch (NullPointerException | RemoteException e) {

        }
    }

    public void close_Block(String trackLine, int blockNum) throws RemoteException {
        System.out.println("close called " + blockNum);
        int controller = find_Controller(trackLine, blockNum);
        ((TrackController[])trackControllerMap.get(trackLine.toLowerCase()))[controller].set_Block_Closed(blockNum);
    }

    public void open_Block(String trackLine, int blockNum) throws RemoteException {
        System.out.println("open called " + blockNum);
        int controller = find_Controller(trackLine, blockNum);
        ((TrackController[])trackControllerMap.get(trackLine.toLowerCase()))[controller].set_Block_Open(blockNum);
    }
    //CTC

    //HARDWARE
    public void get_Block_Array(int start) throws RemoteException {
        /*System.out.println(start);
        hardwareNum = find_Controller("green", start);
        System.out.println("Hardware connected to controller " + (hardwareNum + 1));
        hardwareController = get_Controller("green", hardwareNum);
        System.out.println(hardwareController.get_Switch_ID());*/
    }

    /*public static void send_Block_State(int blockNum, boolean state) throws RemoteException {
        System.out.println("called");
        Network.tchw_Interface.set_Block_State(blockNum, state);
    }*/
    //HARDWARE

    //INTERNAL
    public void set_Block_State_Catalogue(int blockNum) throws FileNotFoundException, RemoteException {
        //System.out.println("Train moved");
        String line = "green";
        //TODO make available for all lines
        int controller = find_Controller(line, blockNum);
        ((TrackController[])trackControllerMap.get(line))[controller].set_Block_State(blockNum);
    }

    public int find_Controller(String trackLine, int blockNum) {
        boolean foundBlock;
        int controller = -1;
        int i = 0;
        ArrayList temp;
        temp = ((ArrayList)controllerBlocksMap.get(trackLine.toLowerCase()));

        if (blockNum == 0) {
            return 3;
        }

        while (controller == -1 && i < temp.size()) {
            temp = (ArrayList) ((ArrayList)controllerBlocksMap.get(trackLine.toLowerCase())).get(i);
            foundBlock = temp.contains(blockNum);
            if (foundBlock)
                controller = i;

            i++;
        }

        //System.out.println("controller index: " + controller);
        return controller;
    }

    public TrackController get_Controller(String trackLine, int num) {
        ((TrackController[])trackControllerMap.get(trackLine.toLowerCase()))[num].set_Controller_Num(num + 1);
        return ((TrackController[])trackControllerMap.get(trackLine.toLowerCase()))[num];
    }
    //INTERNAL

    public void update_Time(double time) {
        System.out.println(time);
    }

}
