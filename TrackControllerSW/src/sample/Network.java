package sample;

import networking.CTC_Interface;
import networking.Track_Controller_HW_Interface;
import networking.Track_Controller_SW_Interface;
import networking.Track_Model_Interface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Network extends TrackControllerCatalogue {

    static boolean connected_Module_1; // CTC
    static boolean connected_Module_2; // Track Model
    static boolean connected_Module_3; // Track Controller HW
    static boolean hosting = false;
    static String module_1_IP = "73.79.47.187"; // Zach IP
    static String module_2_IP = "174.60.84.100"; // Adnan IP
    static String module_3_IP = "73.154.129.166"; // Lee IP
    static int module_1_Port = 1000; // Zach Port
    static int module_2_Port = 1300; // Adnan Port
    static int module_3_Port = 1200; // Lee Port
    //public static Track_Controller_SW_Interface tcsw_Interface; // Train Controller Interface
    public static CTC_Interface c_Interface; // CTC Interface
    public static Track_Model_Interface tm_Interface; // Track Model Interface
    public static Track_Controller_HW_Interface tchw_Interface; // Track Model Interface
    public static TrackControllerCatalogue serverObject;


    public static void start_Server() throws RemoteException {
        try {
            System.setProperty("java.rmi.server.hostname", "71.173.141.165");
            // Instantiating the implementation class
            serverObject = new TrackControllerCatalogue();
            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)

            Track_Controller_SW_Interface stub = (Track_Controller_SW_Interface) UnicastRemoteObject.exportObject(serverObject, 1100);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1100);
            registry.rebind("Track_Controller_SW_Interface", stub);
            System.err.println("Server ready");
            hosting = true;

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

    public static void connect_To_CTC() {
        if (!connected_Module_1) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_1_IP, module_1_Port);
                c_Interface = (CTC_Interface) registry.lookup("CTC_Interface");
                connected_Module_1 = true;
                System.out.println("Connected to CTC");
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static void connect_To_Track_Model() {

        if (!connected_Module_2) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_2_IP, module_2_Port);
                tm_Interface = (Track_Model_Interface) registry.lookup("Track_Model_Interface");
                connected_Module_2 = true;
                System.out.println("Connected to Track Model");
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }


    }

    public static void connect_To_Track_Controller() {
        if (!connected_Module_3) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_3_IP, module_3_Port);
                tchw_Interface = (Track_Controller_HW_Interface) registry.lookup("Track_Controller_HW_Interface");
                //tchw_Interface.receiveTrackSoftware("Box");
                connected_Module_3 = true;
                System.out.println("Connected to Track Hardware");
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
}