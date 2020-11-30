package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PLCConfig {

    public static TrackController con = new TrackController();
    String line;
    Scanner scan;
    File plcFile;
    ArrayList blockNums;

    PLCConfig() {
        blockNums = new ArrayList();
        plcFile = new File("New Text Document.txt");
    }

    public void check_PLC(File plc, ArrayList blocks) throws FileNotFoundException {
        plcFile = plc;
        blockNums = (ArrayList) blocks.clone();

        scan = new Scanner(plcFile);
        while (scan.hasNextLine()) {
            line = scan.nextLine();
            System.out.println(line);
        }
    }

    public boolean switch_Default(boolean blockState) {
        if (blockState)
            return true;
        else
            return false;
    }

    public boolean switch_Other(boolean blockState) {
        if (blockState)
            return false;
        else
            return true;
    }

    public boolean switch_Light() {
        return false;
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
        }
        else {
            return false;
        }
    }

    public void run_PLC_Program(int[] switchPos, boolean[] blockStates, int xBarPos) throws FileNotFoundException {
        scan = new Scanner(plcFile);
        String currentFunction = "";
        int observeBlock = 0;
        int[] xBlocks = new int [7];
        boolean programCheck; //output from internal program
        boolean xCheck = false; //output from crossing check
        boolean plcOut = false; //output from plc
        boolean runChannel2 = false; //if a function ran, run channel 2 check

        while (scan.hasNextLine()) {
            line = scan.nextLine();

            //CHANNEL 1 PLC
            switch (line) {
                case "SWITCH 0": //DEFAULT PATH
                    currentFunction = line;
                    line = scan.nextLine();

                    if (Integer.parseInt(line) == switchPos[0]) {
                        line = scan.nextLine();
                        observeBlock = Integer.parseInt(line.substring(0,3));
                        plcOut = switch_Default(blockStates[blockNums.indexOf(observeBlock)]);
                        runChannel2 = true;
                    } else {
                        System.out.println("SWITCH 0 INVALID");
                    }

                    break;

                case "SWITCH 1": //SWITCH PATH
                    currentFunction = line;
                    line = scan.nextLine();

                    if (Integer.parseInt(line) == switchPos[0]) {
                        line = scan.nextLine();
                        observeBlock = Integer.parseInt(line.substring(0,3));
                        plcOut = switch_Other(blockStates[blockNums.indexOf(observeBlock)]);
                        runChannel2 = true;
                    } else {
                        System.out.println("SWITCH 1 INVALID");
                    }

                    break;

                case "STATION LIGHT": //STATION LIGHT
                    currentFunction = line;
                    line = scan.nextLine();
                    observeBlock = Integer.parseInt(line.substring(0,3));
                    plcOut = station_Light(blockStates[blockNums.indexOf(observeBlock)]);
                    runChannel2 = true;
                    break;

                case "SWITCH LIGHT": //SWITCH LIGHT
                    break;

                case "RAILROAD CROSS": //DEFAULT PATH
                    currentFunction = line;
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
                    }

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
                    case "SWITCH 0": //DEFAULT PATH
                        if (blockStates[blockNums.indexOf(observeBlock)]) {
                            programCheck = true;
                        } else {
                            programCheck = false;
                        }

                        if (comparator(programCheck, plcOut)) {
                            con.set_Switch_State(programCheck);
                        }

                        break;

                    case "SWITCH 1": //SWITCH PATH
                        if (blockStates[blockNums.indexOf(observeBlock)]) {
                            programCheck = false;
                        } else {
                            programCheck = true;
                        }

                        if (comparator(programCheck, plcOut)) {
                            con.set_Switch_State(programCheck);
                        }

                        break;

                    case "STATION LIGHT": //SWITCH PATH
                        if (blockStates[blockNums.indexOf(observeBlock)]) {
                            programCheck = false;
                        } else {
                            programCheck = true;
                        }

                        if (comparator(programCheck, plcOut)) {
                            con.set_Light_State(observeBlock, programCheck);
                        }

                        break;

                    case "SWITCH LIGHT": //SWITCH PATH
                        break;

                    case "RAILROAD CROSS": //DEFAULT PATH
                        int counter = 0;

                        while (xCheck == false && counter < 7) {
                            xCheck = blockStates[blockNums.indexOf(xBlocks[counter])];
                            counter++;
                        }

                        //CHANNEL COMPARATOR
                        if (comparator(xCheck, plcOut)) {
                            con.set_XBar_State(xCheck);
                        }

                        break;

                    case "DEFAULT":
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
