package Back_End;

import networking.Train_Controller_Interface;
import networking.Train_Model_Interface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;


    public class Network extends Train_Controller {

        static boolean connected_Module_1; // Train Model
        static boolean hosting = false;
        static String module_1_IP = "73.154.133.183"; // Sead IP
        static int module_1_Port = 1400; // Sead Port
        static Train_Model_Interface tm_Interface; // Track Model Interface
        public static Train_Controller_Catalogue server_Object;


        public static void start_Server() {
                try {
                    System.out.println(hosting);
                    System.setProperty("java.rmi.server.hostname", "67.171.70.64");

                    // Instantiating the implementation class
                    server_Object = new Train_Controller_Catalogue();

                    // Exporting the object of implementation class
                    // (here we are exporting the remote object to the stub)
                    Train_Controller_Interface stub = (Train_Controller_Interface) UnicastRemoteObject.exportObject(server_Object, 1500);

                    // Binding the remote object (stub) in the registry
                    Registry registry = LocateRegistry.createRegistry(1500);

                    registry.rebind("Train_Controller_Interface", stub);
                    System.err.println("Server ready");
                } catch (Exception e) {
                    System.err.println("Server exception: " + e.toString());
                    e.printStackTrace();
                }
        }



        public static void connect_To_Modules() {
            if (!connected_Module_1) {
                try {
                    Registry registry = LocateRegistry.getRegistry(module_1_IP, module_1_Port);
                    tm_Interface = (Train_Model_Interface) registry.lookup("Train_Model_Interface");
                    connected_Module_1 = true;
                    System.out.println("This is after connected module is true");

                } catch (Exception e) {
                    System.err.println("Client exception: " + e.toString());
                    e.printStackTrace();
                }
            }
        }
    }
