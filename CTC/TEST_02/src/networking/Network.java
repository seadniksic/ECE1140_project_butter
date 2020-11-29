package networking;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import resources.CTC_Back;

public class Network extends CTC_Back {

    static boolean connected_Module_1; // Track Model
    static boolean connected_Module_2;//Sim
    static boolean hosting = false;
    static String module_1_IP = "71.173.141.165"; // Tony IP
    static String module_2_IP = "73.154.133.183"; // Sim IP

    static int module_1_Port = 1100; // Tony Port
    static int module_2_Port = 2000; // Sim port

    static int server_Port = 1000;

    public static Track_Controller_SW_Interface tcsw_Interface; // Track Controller Software Interface
    public static Simulation_Interface Simulation_Interface; // Track Controller Software Interface


    public static CTC_Back server_Object;



    public static void start_Server() throws RemoteException {
        try {
            System.setProperty("java.rmi.server.hostname", "73.79.47.187");
            // Instantiating the implementation class
            server_Object = new CTC_Back();
            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            CTC_Interface stub = (CTC_Interface) UnicastRemoteObject.exportObject(server_Object, server_Port);
            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(server_Port);
            registry.rebind("CTC_Interface", stub);


            System.err.println("Server ready");



            hosting = true;//?ask 'yeah'
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void connect_To_Module() {
        if (!connected_Module_1) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_1_IP, module_1_Port);
                System.out.println("Connecting...");
                tcsw_Interface = (Track_Controller_SW_Interface) registry.lookup("Track_Controller_SW_Interface");

                connected_Module_1 = true;
                if(connected_Module_1) {
                    System.out.println("Connected");
                }
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }

    }

    public static void connect_To_Simulation() {
        if (!connected_Module_2) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_2_IP, module_2_Port);
                System.out.println("Connecting...");
                Simulation_Interface = (Simulation_Interface) registry.lookup("Simulation_Interface");

                connected_Module_2 = true;
                if (connected_Module_2) {
                    System.out.println("Connected");
                }
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

}