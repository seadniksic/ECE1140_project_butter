package sample;

import networking.Track_Controller_SW_Interface;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class TrackControllerCatalogue implements Track_Controller_SW_Interface {
    //HashMap trackController;
    int redControllerNum, greenControllerNum;
    ArrayList<ArrayList<Integer>> controllerBlocks;

    static TrackController [] trackControllers = new TrackController[15];
    TrackController hardwareController = new TrackController();
    int hardwareNum = 0;

    public TrackControllerCatalogue() {
        //greenControllerNum = 6;
        //greenControllerBlocks = new ArrayList();
        //redControllerBlocks = new ArrayList();
        controllerBlocks = new ArrayList();
    }

    //TRACK MODEL
    public void make_Controllers(String[] lines, int[][] blocks) {
        greenControllerNum = blocks[0].length/6;
        //redControllerNum = blocks[1].length/6;

        //Green Line
        //c1 - 1-9, 12-10, 13-20
        //c2 - 29-21, 30-45, 150-117
        //c3 - 46-51, 52-57, 58-60
        //c4 - 62-61, 63-66, 67-69
        //c5 - 76-70, 77-82, 101-116
        //c6 - 85-83, 86-93, 100-94
        for (int line = 0; line < lines.length; line++) {

            for (int controller = 0; controller < blocks[line].length/6; controller++) {
                ArrayList controllerBlock = new ArrayList();

                for (int i = controller*6; i < controller*6 + 6; i+=2) {
                    if (blocks[0][i] < blocks[0][i+1]) {

                        for (int j = blocks[0][i]; j < blocks[0][i+1]+1; j++) {
                            controllerBlock.add(j);
                        }

                    }else if (blocks[0][i] > blocks[0][i+1]) {

                        for (int j = blocks[0][i+1]; j < blocks[0][i]+1; j++) {
                            controllerBlock.add(j);
                        }
                    }
                }

                trackControllers[controller] = new TrackController(controllerBlock);
                controllerBlocks.add(controllerBlock);
                System.out.println(controllerBlock);
                controllerBlock = null;
            }
        }
    }

    public void train_Moved (int trainNum, int blockNum) throws RemoteException, FileNotFoundException {
        //Network.c_Interface.train_Moved(trainNum, blockNum);
        System.out.println("Train " + trainNum + " moved to block " + blockNum);
        set_Block_State_Catalogue(blockNum);

        /*if (find_Controller("green", blockNum) == hardwareNum) {
            send_Block_State(blockNum, true);
            if (blockNum > hardwareController.get_Block_ID(0))
                send_Block_State(blockNum-1, false);
        }*/
    }

    public void set_Broken_Rail(int lineIndex, int blockNum, boolean state) throws RemoteException {
        int controller = 0;

        if (lineIndex == 0) {
            controller = find_Controller("green", blockNum);
        } else if (lineIndex == 1) {
            controller = find_Controller("red", blockNum);
        }

        trackControllers[controller].set_Broken_Rail(blockNum, state);
    }


    public void set_Track_Circuit_Failure(int lineIndex, int blockNum, boolean state) throws RemoteException {
        int controller = 0;

        if (lineIndex == 0) {
            controller = find_Controller("green", blockNum);
        } else if (lineIndex == 1) {
            controller = find_Controller("red", blockNum);
        }

        trackControllers[controller].set_Track_Fail(blockNum, state);
    }


    public void set_Power_Fail(int lineIndex, int blockNum, boolean state) throws RemoteException {
        int controller = 0;

        if (lineIndex == 0) {
            controller = find_Controller("green", blockNum);
        } else if (lineIndex == 1) {
            controller = find_Controller("red", blockNum);
        }

        trackControllers[controller].set_Power_Fail(blockNum, state);
    }
    //TRACK MODEL

    //CTC
    public void create_Train(int cars, String line, int block) throws RemoteException {
        System.out.println("I will go to Track Model with " + cars + " cars");
        System.out.println("to line " + line);
        System.out.println("to yard on " + block);
        //Network.tm_Interface.create_Train(cars);
    } // (CTC -> track controller ->) track model -> train model

    public void remove_Train(int trainNum) throws RemoteException {
        //Network.tm_Interface.send_Beacon_Information(train_Num);
    }

    public void send_Beacon_Information(int train_Num, String next_Stop) throws RemoteException {
        //Network.tm_Interface.send_Beacon_Information(train_Num, next_Stop);
    }

    public void send_Speed_Authority(int train_Num, double speed, int authority) {
        System.out.println("I will go to Track Model");
        System.out.println("Train Number: " + train_Num);
        System.out.println("Speed: " + speed);
        System.out.println("Au: " + authority);

        //Network.tm_interface.send_Speed_Authority(); //call track models send method
    } // (CTC -> track controller ->) track model -> train model

    public boolean set_Switch_Manual(String trackLine, int blockNum, boolean state) throws RemoteException {
        int controller = find_Controller(trackLine, blockNum);
        trackControllers[controller].set_Switch_State(state);
        return true;
    }

    public static void add_Ticket(int trainNum) throws RemoteException {
        Network.c_Interface.add_Ticket_CTC(trainNum);
    }

    public void close_Block(String trackLine, int blockNum) throws RemoteException {
        System.out.println("close called " + blockNum);
        int controller = find_Controller(trackLine, blockNum);
        trackControllers[controller].set_Block_Closed(blockNum);
    }

    public void open_Block(String trackLine, int blockNum) throws RemoteException {
        System.out.println("open called " + blockNum);
        int controller = find_Controller(trackLine, blockNum);
        trackControllers[controller].set_Block_Open(blockNum);
    }
    //CTC

    //HARDWARE
    public void get_Block_Array(int start) throws RemoteException {
        System.out.println(start);
        hardwareNum = find_Controller("green", start);
        hardwareController = get_Controller(hardwareNum);
    }

    public static void send_Block_State(int blockNum, boolean state) throws RemoteException {
        System.out.println("called");
        Network.tchw_Interface.set_Block_State(blockNum, state);
    }
    //HARDWARE

    //INTERNAL
    public void set_Block_State_Catalogue(int blockNum) throws FileNotFoundException {
        System.out.println("Train moved");
        int controller = find_Controller("green", blockNum);
        trackControllers[controller].set_Block_State(blockNum);
    }

    public int find_Controller(String trackLine, int blockNum) {
        boolean foundBlock;
        int controller = 0;

        if (trackLine.equalsIgnoreCase("green")) {
            for (int i = 0; i < greenControllerNum; i++) {

                foundBlock = controllerBlocks.get(i).contains(blockNum);
                if (foundBlock)
                    controller = i;
            }
        } else if (trackLine.equalsIgnoreCase("red")) {
            for (int i = 0; i < redControllerNum; i++) {

                foundBlock = controllerBlocks.get(i+6).contains(blockNum);
                if (foundBlock)
                    controller = i;
            }
        }

        return controller;
    }

    public static TrackController get_Controller(int num) {
        trackControllers[num].set_Controller_Num(num + 1);
        return trackControllers[num];
    }

    public int get_Controller_Num() {
        return greenControllerNum;
    }
    //INTERNAL

    public void update_Time(double time) {
        System.out.println(time);
    }

}
