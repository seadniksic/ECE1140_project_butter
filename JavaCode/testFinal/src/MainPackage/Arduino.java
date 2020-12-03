package MainPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class Arduino<in> {

    //static ArduinoConnector connect = null;
    public static String send;
    public static String input;
    public static InputStream in = InitialController.chosenPort.getInputStream();
    public static PrintWriter output = new PrintWriter(InitialController.chosenPort.getOutputStream());

    String msg = "";

    public static char[] ArduinoMessenger(String send) throws IOException {

        int reply = 0;

        output.println(send);
        output.flush();
        System.out.println("I Arduino sent: " + send);

        //Wait 1/10 second
        try{ Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }

        boolean recvInProgress = false;
        byte ndx = 0;
        byte startMarker = 0x3C; //Start marker is <
        byte endMarker = 0x3E;  //End marker is >
        char rb;
        byte numBytes = 64;
        char[] receivedBytes = new char[numBytes];
        String MSBit;

        byte numReceived = 0;
        boolean newData = false;
        boolean MSB = true;

        while (in.available() > 0 && !newData) {
            rb = (char)in.read(); //read the first byte to readbyte

            if (recvInProgress == true) {
                if (rb != endMarker) {

                    receivedBytes[ndx] = (char) rb;
                    ndx++;
                    if (ndx >= numBytes) {
                        ndx = (byte) (numBytes - 1);
                    }
                } else {
                    receivedBytes[ndx] = '\0'; // terminate the string
                    recvInProgress = false;
                    numReceived = ndx;  // save the number for use when printing
                    ndx = 0;
                    newData = true;

                }
            } else if (rb == startMarker) {
                recvInProgress = true;
            }

        }







        //Read Reply


        return receivedBytes;


        //Display messages
        //System.out.println("I received back: " + reply);
        //for (int j = 0; j < 8; j++){
        //    System.out.print(input[j]);
        //}
        //System.out.println(" ");

        //System.out.println(input.length);

        //reply = "";
        // length is the number of bytes returned
        //int messageLength = input.length;
        /*
        for (int j = 0; j < 8; j++){
            int integerValue = input[j];
            //char character = (char) integerValue;
            //byte byteValue = (byte) integerValue;
            //String s1 = String.format("%8s", Integer.toBinaryString(byteValue & 0xFF)).replace(' ', '0');
            reply = reply + integerValue;
            //System.out.println(character+" = "+ integerValue + " Binary : " + s1); // print the character and it's value in ascii
        }\
        */


    }







};
//insert commented part here




