package networking;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import resources.Train_Model_Catalogue;

public class Network extends Train_Model_Catalogue {

    public static boolean connected_Module_1; // Track Model
    public static boolean connected_Module_2; // Train Controller
    public static boolean serving = false;
    public static String module_1_IP = "174.60.84.100"; // Adnan IP
    public static String module_2_IP = "67.171.70.64"; // Meyers IP
    public static int module_1_Port = 1300; // Adnan Port
    public static int module_2_Port = 1500; // Meyers Port
    public static Train_Controller_Interface tc_Interface; // Train Controller Interface
    public static Track_Model_Interface tm_Interface; // Track Model Interface
    public static Train_Model_Catalogue server_Object;


    public static void start_Server() throws RemoteException {
        try {
            System.setProperty("sun.rmi.transport.tcp.responseTimeout", "5000");
            System.setProperty("java.rmi.server.hostname", "73.154.133.183");
            // Instantiating the implementation class
            server_Object = new Train_Model_Catalogue();

            // (here we are exporting the remote object to the stub)
            Train_Model_Interface stub = (Train_Model_Interface) UnicastRemoteObject.exportObject(server_Object, 1400);
            //Train_Model_Interface stub2 = (Train_Model_Interface) UnicastRemoteObject.exportObject(server_Object, 1399);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1400);
            registry.rebind("Train_Model_Interface", stub);
            //registry.rebind("Train_Model_Interface", stub2);

            System.err.println("Server ready");
            serving = true;

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void connect_To_Modules() {
//        if (!connected_Module_1) {
//            try {
//                Registry registry = LocateRegistry.getRegistry(module_1_IP, module_1_Port);
//                tm_Interface = (Track_Model_Interface) registry.lookup("Track_Model_Interface");
//                connected_Module_1 = true;
//            } catch (Exception e) {
//                System.err.println("Client exception: " + e.toString());
//                e.printStackTrace();
//            }
//        }
        if (!connected_Module_2) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_2_IP, module_2_Port);
                tc_Interface = (Train_Controller_Interface) registry.lookup("Train_Controller_Interface");
                connected_Module_2 = true;
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
}