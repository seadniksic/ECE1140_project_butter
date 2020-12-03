package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class PLCConfig {

    public static TrackController con = new TrackController();
    String line;
    Scanner scan;
    File plcFile;
    ArrayList blockNums;
    boolean prevBlockState;
    boolean switchLight1, switchLight2, switchLight3;
    boolean lightChange = false;

    PLCConfig() {
        blockNums = new ArrayList();
        plcFile = new File("New Text Document.txt");
        prevBlockState = false;
        switchLight1 = false;
        switchLight2 = false;
        switchLight3 = false;
    }

    public void check_PLC(File plc, ArrayList blocks) throws FileNotFoundException {
        plcFile = plc;
        blockNums = (ArrayList) blocks.clone();

        /*scan = new Scanner(plcFile); //print out contents of file to console
        while (scan.hasNextLine()) {
            line = scan.nextLine();
            System.out.println(line);
        }*/
    }

    public boolean switch_Change(boolean blockState, boolean currentSwitchState) {
        System.out.println("Block " + prevBlockState);
        System.out.println("State "  + blockState);
        System.out.println("Switch " + currentSwitchState);
        if (prevBlockState && blockState == false && currentSwitchState == false) {
            return true;
        } else if (prevBlockState && blockState == false && currentSwitchState) {
            return false;
        } else {
            return false;
        }
    }

    public void switch_Light(boolean currentSwitchState) {
        if (!currentSwitchState) {
            switchLight1 = true;
            switchLight2 = true;
            switchLight3 = false;
        } else {
            switchLight1 = true;
            switchLight2 = false;
            switchLight3 = true;
        }
    }

    public boolean station_Light(boolean blockState) {
        return !blockState;
    }

    public boolean xBar (boolean[] blockStates, int[] xBlocks) {
        int counter = 0;
        boolean xCheck = false;

        while (xCheck == false && counter < 7) {
            xCheck = blockStates[blockNums.indexOf(xBlocks[counter])];
            counter++;
        }

        return xCheck;
    }

    public boolean comparator(boolean pc, boolean plc) {
        if (pc == plc) {
            return true;
        } else {
            return false;
        }
    }

    public void run_PLC_Program(int[] switchPos, boolean[] blockStates, boolean switchState, int xBarPos) throws FileNotFoundException, RemoteException {
        scan = new Scanner(plcFile);
        String currentFunction = "";

        int observeBlock = 0;
        int observeBlockDefault = 0;
        int observeBlockOther = 0;

        int[] xBlocks = new int [7];

        boolean programCheck = false; //output from internal program
        boolean xCheck = false; //output from crossing check
        boolean plcOut = false; //output from plc
        boolean runChannel2 = false; //if a function ran, run channel 2 check

        System.out.println("SWITCH IS CURRENTLY: " + switchState);
        while (scan.hasNextLine()) {
            line = scan.nextLine();

            //CHANNEL 1 PLC
            switch (line) {
                case "SWITCH SET": //DEFAULT PATH
                    currentFunction = line;
                    line = scan.nextLine();

                    observeBlockOther = Integer.parseInt(line.substring(0,3));
                    line = scan.nextLine();
                    observeBlockDefault = Integer.parseInt(line.substring(0,3));

                    //System.out.println(observeBlockOther);
                    //System.out.println(observeBlockDefault);
                    if (!switchState) {
                        plcOut = switch_Change(blockStates[blockNums.indexOf(observeBlockOther)], switchState);
                    } else {
                        plcOut = switch_Change(blockStates[blockNums.indexOf(observeBlockDefault)], switchState);
                    }

                    System.out.println("Output " + plcOut);
                    runChannel2 = true;
                    break;

                case "STATION LIGHT": //STATION LIGHT
                    /*currentFunction = line;
                    line = scan.nextLine();
                    observeBlock = Integer.parseInt(line.substring(0,3));
                    plcOut = station_Light(blockStates[blockNums.indexOf(observeBlock)]);
                    runChannel2 = true;*/
                    break;

                case "SWITCH LIGHT": //SWITCH LIGHT
                    /*currentFunction = line;
                    switch_Light(lightChange);
                    runChannel2 = true;*/
                    break;

                case "RAILROAD CROSS": //DEFAULT PATH
                    /*currentFunction = line;
                    line = scan.nextLine();

                    if (Integer.parseInt(line) == xBarPos) {
                        for (int i = 0; i < 7; i++) {
                            line = scan.nextLine();
                            xBlocks[i] = Integer.parseInt(line.substring(0,3));
                        }
                        plcOut = xBar(blockStates, xBlocks);
                        runChannel2 = true;
                    } else {
                        System.out.println("XBar INVALID");
                    }*/

                    break;

                case "DEFAULT":
                    break;

                default:
                    break;
            }

            //DIVERSITY/REDUNDANCY
            //CHANNEL 2
            if (runChannel2) {

                switch (currentFunction) {
                    case "SWITCH SET": //DEFAULT PATH

                        if (!switchState) {
                            if (blockStates[blockNums.indexOf(observeBlockOther)] == false && prevBlockState && !switchState) {
                                programCheck = true;//!switchState;
                            } else {
                                programCheck = false;//switchState;
                            }
                            prevBlockState = blockStates[blockNums.indexOf(observeBlockOther)];
                        } else {
                            if (blockStates[blockNums.indexOf(observeBlockDefault)] == false && prevBlockState && switchState) {
                                programCheck = false;//!switchState;
                            } else {
                                programCheck = true;//switchState;
                            }
                            prevBlockState = blockStates[blockNums.indexOf(observeBlockDefault)];
                        }

                        //System.out.println("SWITCH PENDING");

                        if (comparator(programCheck, plcOut)) {
                            System.out.println("C2 Output " + programCheck);
                            con.set_Switch_State(programCheck);
                            lightChange = programCheck;
                            /*try {
                                Network.tm_Interface.set_Switch_At_Block(0, switchPos[0], programCheck);
                            } catch (NullPointerException | RemoteException e) {
                                e.printStackTrace();
                            }*/
                        }
                        break;

                    case "STATION LIGHT": //SWITCH PATH
                        /*if (blockStates[blockNums.indexOf(observeBlock)]) {
                            programCheck = false;
                        } else {
                            programCheck = true;
                        }

                        if (comparator(programCheck, plcOut)) {
                            con.set_Light_State(observeBlock, programCheck);
                        }*/
                        break;

                    case "SWITCH LIGHT": //SWITCH PATH
                        /*if (!lightChange) {
                            programCheck = true;
                        } else {
                            programCheck = false;
                        }
                        //System.out.println("switch is " + programCheck);
                        //CHANNEL COMPARATOR
                        if (comparator(programCheck, switchLight2)) {
                            //System.out.println("switch");
                            con.set_Light_State(switchPos[0], switchLight1);
                            con.set_Light_State(switchPos[1], switchLight2);
                            con.set_Light_State(switchPos[2], switchLight3);
                        }*/
                        break;

                    case "RAILROAD CROSS": //DEFAULT PATH
                        /*int counter = 0;

                        while (xCheck == false && counter < 7) {
                            xCheck = blockStates[blockNums.indexOf(xBlocks[counter])];
                            counter++;
                        }

                        //CHANNEL COMPARATOR
                        if (comparator(xCheck, plcOut)) {
                            con.set_XBar_State(xCheck);
                            /*try {
                                Network.tm_Interface.set_Crossbar_At_Block(0, con.get_XBar_ID(), xCheck);
                            } catch (NullPointerException | RemoteException e) {
                                //e.printStackTrace();
                            }*/
                        //}
                        break;

                    default:
                        break;
                }

            }
            //DIVERSITY/REDUNDANCY
            runChannel2 = false;
        }
    }


}
