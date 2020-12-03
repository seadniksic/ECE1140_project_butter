package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.*;

public class TrackController {
    int controllerNum;
    String trackLine;
    ArrayList blocks; //holds block numbers
    boolean[] blockStates; //holds each block occupancy
    int[] switchPos; //holds where switch is
    boolean[] switchState; //holds each switch block state
    int xBarPos; //holds where crossbar is if exists
    boolean xBarState; //holds crossbar state
    ArrayList lightPos; //holds light positions (every switch related block, station)
    boolean [] trafficLights; //holds light states

    //holds faults per block
    String [] isBrokenRail;
    String [] isTrackCircuitFail;
    String [] isPowerFail;
    //holds faults per block

    boolean [] isOpen; //holds which blocks are open

    //PLC related variables
    File plc;
    PLCConfig plcConfig;

    public TrackController() {
        blocks = new ArrayList();
        blockStates = new boolean[10];
        isBrokenRail = new String[10];
        isTrackCircuitFail = new String[10];
        isPowerFail = new String[10];
        isOpen = new boolean[10];
        switchPos = new int[3];
        switchState = new boolean[3];
        lightPos = new ArrayList();
        trafficLights = new boolean[10];
        xBarState = false;
        plc = new File(get_Track_Line() + get_Controller_Num() + ".txt");
        plcConfig = new PLCConfig();
    }

    public TrackController(ArrayList Blocks) {
        blocks = new ArrayList();
        blockStates = new boolean[Blocks.size()];
        isBrokenRail = new String[Blocks.size()];
        isTrackCircuitFail = new String[Blocks.size()];
        isPowerFail = new String[Blocks.size()];
        isOpen = new boolean[Blocks.size()];
        switchPos = new int[3];
        switchState = new boolean[3];
        lightPos = new ArrayList();
        trafficLights = new boolean[Blocks.size()];
        xBarState = false;
        plc = new File("green" + get_Controller_Num() + ".txt");
        plcConfig = new PLCConfig();

        blocks = (ArrayList) Blocks.clone();

        for (int i = 0; i < Blocks.size(); i++) { //initialize per block
            //blocks.add(Blocks.get(i));
            blockStates[i] = false;
            isBrokenRail[i] = "No BR";
            isTrackCircuitFail[i] = "No TCF";
            isPowerFail[i] = "No PF";
            isOpen[i] = true;
            trafficLights[i] = true;
        }
    }

    public void set_Signals() throws FileNotFoundException {
        Scanner scan = new Scanner(plc);
        String line;

        while (scan.hasNextLine()) {
            line = scan.nextLine();

            switch (line) {
                case "SWITCH": //
                    int i = 0;
                    line = scan.nextLine();
                    do {
                        switchPos[i] = Integer.parseInt(line.substring(0,3));
                        lightPos.add(switchPos[i]);
                        switch (i) {
                            case 0:
                            case 1:
                                switchState[i] = false;
                                break;
                            case 2:
                                switchState[i] = true;
                                break;
                            default: break;
                        }
                        i++;
                        line = scan.nextLine();
                    } while (!line.equalsIgnoreCase("END"));

                    break;

                case "STATION": //
                    line = scan.nextLine();
                    do {
                        lightPos.add(Integer.parseInt(line.substring(0,3)));
                        line = scan.nextLine();
                    } while (!line.equalsIgnoreCase("END"));
                    break;


                case "CROSSING": //
                    line = scan.nextLine();
                    xBarPos = Integer.parseInt(line.substring(0,3));
                    xBarState = false;
                    break;

                default:
                    break;
            }
        }

        lightPos.sort(Comparator.naturalOrder());
        System.out.println(lightPos);
        for (int i = 0; i < blocks.size(); i++) {
            if (lightPos.contains(blocks.get(i))) {
                trafficLights[i] = true;
            } else {
                trafficLights[i] = false;
            }

            if ((int)blocks.get(i) == switchPos[2]) {
                trafficLights[i] = false;
            }
        }
    }

    public void set_Controller_Num(int num) {
        controllerNum = num;
    }

    public void set_Track_Line(String line) {
        trackLine = line;
    }

    //change internal block state when train moves, run PLC
    public void set_Block_State(int blockNum) throws FileNotFoundException, RemoteException {
        reset_Blocks();

        if (blockNum != 0)
        blockStates[blocks.indexOf(blockNum)] = true;

        /*if (blocks.indexOf(blockNum) == 0) {
            blockStates[blocks.indexOf(blockNum+1)] = false;
        } else if (blocks.indexOf(blockNum) == blocks.size()-1) {
            blockStates[blocks.indexOf(blockNum-1)] = false;
        } else {
            blockStates[blocks.indexOf(blockNum-1)] = false;
            blockStates[blocks.indexOf(blockNum+1)] = false;
        }*/

        run_PLC();
    }

    //plc calls to change internal switch
    public void set_Switch_State(boolean newSwitch) throws RemoteException {
        switchState[0] = newSwitch;
        try {
            Network.tm_Interface.set_Switch_At_Block(0, switchPos[0], newSwitch);
        } catch (NullPointerException | RemoteException e) {

        }
    }

    //plc calls to change internal lights
    public void set_Light_State(int blockNum, boolean newLight) throws RemoteException {
        if (blockNum != 0)
        trafficLights[blocks.indexOf(blockNum)] = newLight;
        try {
            Network.c_Interface.change_Lights(get_Track_Line(), blockNum, newLight);
        } catch (NullPointerException | RemoteException e) {

        }

    }

    public void set_XBar_State(boolean newBar) throws RemoteException {
        xBarState = newBar;
        try {
            Network.c_Interface.change_CrossBar(get_Track_Line(), xBarPos, newBar);
        }catch (NullPointerException | RemoteException e) {

        }

    }

    public void set_Block_Open(int blockNum) {
        isOpen[blocks.indexOf(blockNum)] = true;
    }

    public void set_Block_Closed(int blockNum) {
        isOpen[blocks.indexOf(blockNum)] = false;
    }

    public void set_PLC_Program(String path) throws FileNotFoundException {
        plc = new File(path);
        set_Signals();
        plcConfig.check_PLC(plc, blocks);
    }

    public void set_Broken_Rail(int blockNum, boolean newState) {
        if (newState) {
            isBrokenRail[blocks.indexOf(blockNum)] = "Broken Rail";
            isOpen[blocks.indexOf(blockNum)] = false;
        } else {
            isBrokenRail[blocks.indexOf(blockNum)] = "No BR";
            isOpen[blocks.indexOf(blockNum)] = true;
        }
    }

    public void set_Track_Fail(int blockNum, boolean newState) {
        if (newState) {
            isTrackCircuitFail[blocks.indexOf(blockNum)] = "Track Circuit Fail";
            isOpen[blocks.indexOf(blockNum)] = false;
        } else {
            isTrackCircuitFail[blocks.indexOf(blockNum)] = "No TCF";
            isOpen[blocks.indexOf(blockNum)] = true;
        }
    }

    public void set_Power_Fail(int blockNum, boolean newState) {
        if (newState) {
            isPowerFail[blocks.indexOf(blockNum)] = "Power Fail";
            isOpen[blocks.indexOf(blockNum)] = false;
        } else {
            isPowerFail[blocks.indexOf(blockNum)] = "No PF";
            isOpen[blocks.indexOf(blockNum)] = true;
        }
    }

    public void run_PLC () throws FileNotFoundException, RemoteException {
        plcConfig.run_PLC_Program(switchPos, blockStates, switchState[0], xBarPos);
    }

    //FOR UI USE ONLY
    public int get_Controller_Num() { return controllerNum; }

    public String get_Track_Line() { return trackLine; }

    public int get_Block_Amount() { return blocks.size(); }

    public int get_Block_ID(int blockIndex) { return (int) blocks.get(blockIndex); }

    public boolean get_Block_State(int blockIndex) { return blockStates[blockIndex]; }

    public boolean get_Block_Status(int blockIndex) { return isOpen[blockIndex]; }

    public int get_Switch_ID() { return switchPos[0]; }

    public int get_Switch_Beta() { return switchPos[1]; }

    public int get_Switch_Gamma() { return switchPos[2]; }

    public boolean get_Switch_State() { return switchState[0]; }

    public String get_Light_State(int blockIndex) {
        if (trafficLights[blockIndex] && lightPos.contains(blocks.get(blockIndex))) {
            return "Green Light";
        } else if (!trafficLights[blockIndex] && lightPos.contains(blocks.get(blockIndex))) {
            return "Red Light";
        } else {
            return "";
        }
    }

    public int get_XBar_ID() { return xBarPos; }

    public boolean get_XBar_State() { return xBarState; }

    public String get_Broken_Rail(int blockIndex) { return isBrokenRail[blockIndex]; }

    public String get_Track_Fail(int blockIndex) { return isTrackCircuitFail[blockIndex]; }

    public String get_Power_Fail(int blockIndex) { return isPowerFail[blockIndex]; }

    public String get_File() {
        if (plc.exists()) {
            return plc.getPath();
        } else {
            return "No File";
        }
    }
    //FOR UI USE ONLY

    //DEBUG FUNCTIONS
    public void reset_Blocks() {
        for (int i = 1; i < blocks.size()+1; i++) {
            blockStates[i-1] = false;
        }
    }
}

