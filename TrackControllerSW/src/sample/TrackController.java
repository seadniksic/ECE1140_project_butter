package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static sample.Network.tm_Interface;

public class TrackController {
    int controllerNum;
    String trackLine;
    ArrayList blocks; //holds block numbers
    boolean[] blockStates; //holds each block occupancy
    int[] switchPos; //holds where switch is
    //int switchPos; //holds where switch is
    boolean[] switchState;
    //static boolean switchState; //holds switch position
    int xBarPos; //holds where crossbar is if exists
    boolean xBarState; //holds crossbar state
    ArrayList lightPos; //holds light positions (every switch related block, station)
    String [] trafficLights; //holds light states

    //holds faults per block
    String [] isBrokenRail;
    String [] isTrackCircuitFail;
    String [] isPowerFail;
    //holds faults per block

    boolean [] isOpen; //holds which blocks are open
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
        trafficLights = new String[10];
        xBarState = false;
        plc = new File("New Text Document.txt");
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
        trafficLights = new String[Blocks.size()];
        xBarState = false;
        plc = new File("New Text Document.txt");
        plcConfig = new PLCConfig();

        blocks = (ArrayList) Blocks.clone();

        for (int i = 0; i < Blocks.size(); i++) { //initialize per block
            //blocks.add(Blocks.get(i));
            blockStates[i] = false;
            isBrokenRail[i] = "No BR";
            isTrackCircuitFail[i] = "No TCF";
            isPowerFail[i] = "No PF";
            isOpen[i] = true;
            trafficLights[i] = "Green";
        }
    }

    public void set_Signals() throws FileNotFoundException {
        Scanner scan = new Scanner(plc);
        String line;

        while (scan.hasNextLine()) {
            line = scan.nextLine();

            switch (line) {
                case "SWITCH": //DEFAULT PATH
                    int i = 0;
                    line = scan.nextLine();
                    do {
                        //System.out.println(line + " " + i);
                        switchPos[i] = Integer.parseInt(line.substring(0,3));
                        lightPos.add(switchPos[i]);
                        switch (i) {
                            case 0:
                            case 1:
                                switchState[i] = true;
                                break;
                            case 2:
                                switchState[i] = false;
                                break;
                            default: break;
                        }
                        i++;
                        line = scan.nextLine();
                    } while (!line.equalsIgnoreCase("END"));
                    break;

                case "STATION": //SWITCH PATH
                    line = scan.nextLine();
                    do {
                        lightPos.add(Integer.parseInt(line.substring(0,3)));
                        line = scan.nextLine();
                    } while (!line.equalsIgnoreCase("END"));
                    break;


                case "CROSSING": //SWITCH PATH
                    line = scan.nextLine();
                    xBarPos = Integer.parseInt(line.substring(0,3));
                    //lightPos.add(xBarPos);
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
                trafficLights[i] = "Green Light";
            } else {
                trafficLights[i] = "";
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
    public void set_Block_State(int blockNum) throws FileNotFoundException {
        blockStates[blocks.indexOf(blockNum)] = true;

        if (blockNum == (int) blocks.get(0))
            blockStates[blocks.indexOf(blockNum+1)] = false;

        if (blockNum == blocks.indexOf(blocks.size() - 1))
            blockStates[blocks.indexOf(blockNum-1)] = false;

        if (blockNum > (int) blocks.get(0) && blockNum < blocks.indexOf(blocks.size() - 1)){
            blockStates[blocks.indexOf(blockNum-1)] = false;
            blockStates[blocks.indexOf(blockNum+1)] = false;
        }

        run_PLC();
    }

    //plc calls to change internal switch
    public void set_Switch_State(boolean newSwitch) {
        switchState[0] = newSwitch;
    }

    //plc calls to change internal lights
    public void set_Light_State(int blockNum, boolean newLight) {
        if (newLight) {
            trafficLights[blocks.indexOf(blockNum)] = "Green Light";
        } else {
            trafficLights[blocks.indexOf(blockNum)] = "Red Light";
        }
    }

    public void set_XBar_State(boolean newBar) {
        xBarState = newBar;
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
        } else {
            isBrokenRail[blocks.indexOf(blockNum)] = "No BR";
        }
    }

    public void set_Track_Fail(int blockNum, boolean newState) {
        if (newState) {
            isTrackCircuitFail[blocks.indexOf(blockNum)] = "Track Circuit Fail";
        } else {
            isTrackCircuitFail[blocks.indexOf(blockNum)] = "No TCF";
        }
    }

    public void set_Power_Fail(int blockNum, boolean newState) {
        if (newState) {
            isPowerFail[blocks.indexOf(blockNum)] = "Power Fail";
        } else {
            isPowerFail[blocks.indexOf(blockNum)] = "No PF";
        }
    }

    public void run_PLC () throws FileNotFoundException {
        plcConfig.run_PLC_Program(switchPos, blockStates, xBarPos);
    }

    //FOR UI USE ONLY
    public int get_Block_Amount() {
        return blocks.size();
    }

    public int get_Block_ID(int blockIndex) {
        return (int) blocks.get(blockIndex);
    }

    public boolean get_Block_State(int blockIndex) {
        return blockStates[blockIndex];
    }

    public boolean get_Block_Status(int blockIndex) {
        return isOpen[blockIndex];
    }

    public int get_Switch_ID() {
        //return 0;
        return switchPos[0];
    }

    public boolean get_Switch_State() {
        //return true;
        return switchState[0];
    }
    public String get_Light_State(int blockIndex) {
        return trafficLights[blockIndex];
    }

    public int get_XBar_ID() {
        return xBarPos;
    }

    public boolean get_XBar_State() {
        return xBarState;
    }

    public String get_Broken_Rail(int blockIndex) {
        return isBrokenRail[blockIndex];
    }

    public String get_Track_Fail(int blockIndex) {
        return isTrackCircuitFail[blockIndex];
    }

    public String get_Power_Fail(int blockIndex) {
        return isPowerFail[blockIndex];
    }

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

