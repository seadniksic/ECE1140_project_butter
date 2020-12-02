package sample;

import networking.Track_Controller_SW_Interface;
import networking.Track_Model_Interface;
import networking.Train_Model_Interface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Network extends Track_Model_Murphy_GUI {

    public static boolean connected_Module_1; // Track Controller
    public static boolean connected_Module_2; // Train Model
    public static boolean serving = false;
    public static String module_1_IP = "71.173.141.165"; // Tony IP
    public static String module_2_IP = "73.154.133.183"; // Sead IP
    public static int module_1_Port = 1100; // Tony Port
    public static int module_2_Port = 1400; // Sead Port
    public static Track_Controller_SW_Interface tcs_Interface; // Train Controller Interface
    public static Train_Model_Interface tm_Interface;
    public static Track_Model_Murphy_GUI server_Object;


    public static void start_Server() throws RemoteException {
        try {
            System.setProperty("sun.rmi.transport.tcp.responseTimeout", "5000");
            System.setProperty("java.rmi.server.hostname", "174.60.84.100");
            // Instantiating the implementation class
            server_Object = new Track_Model_Murphy_GUI();

            // (here we are exporting the remote object to the stub)
            Track_Model_Interface stub = (Track_Model_Interface) UnicastRemoteObject.exportObject(server_Object, 1300);
            //Train_Model_Interface stub2 = (Train_Model_Interface) UnicastRemoteObject.exportObject(server_Object, 1399);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1300);
            registry.rebind("Track_Model_Interface", stub);
            //registry.rebind("Train_Model_Interface", stub2);

            System.err.println("Server ready");
            serving = true;

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void connect_To_Modules() {
        if (!connected_Module_1) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_1_IP, module_1_Port);
                tcs_Interface = (Track_Controller_SW_Interface) registry.lookup("Track_Controller_SW_Interface");
                connected_Module_1 = true;
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
        if (!connected_Module_2) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_2_IP, module_2_Port);
                tm_Interface = (Train_Model_Interface) registry.lookup("Train_Model_Interface");
                connected_Module_2 = true;
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }

    }
}