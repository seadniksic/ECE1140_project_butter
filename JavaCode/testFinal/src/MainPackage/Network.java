package MainPackage;

import networking.Track_Controller_HW_Interface;
import networking.Track_Controller_SW_Interface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Network extends Main {

    static boolean connected_Module_1; // Train_Controller_SW_Interface

    static boolean hosting = false;
    static String module_1_IP = "71.173.141.165"; // tony IP

    static int module_1_Port = 1100; // tony Port

    public static Track_Controller_SW_Interface tc_Interface; // Train Controller Interface

    public static Main server_Object;

    public static void start_Server() {
        try {
            System.setProperty("java.rmi.server.hostname", "73.154.129.166");

            // Instantiating the implementation class
            server_Object = new Main();

            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            //Registry registry = LocateRegistry.getRegistry(1100);

            Track_Controller_HW_Interface stub = (Track_Controller_HW_Interface) UnicastRemoteObject.exportObject(server_Object, 1200);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1200);
            registry.bind("Track_Controller_HW_Interface", stub);
            System.err.println("Server ready");
            hosting = true;

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void connect_To_Modules() {
        if (!connected_Module_1) {
            try {
                Registry registry = LocateRegistry.getRegistry(module_1_IP, module_1_Port);
                tc_Interface = (Track_Controller_SW_Interface) registry.lookup("Track_Controller_SW_Interface");
                System.out.println("Track Controller Software Connected");
                connected_Module_1 = true;
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }

    }







    /*
    @Override
    public boolean lightStatus() throws RemoteException {
        return false;
    }

    @Override
    public boolean barrierStatus() throws RemoteException {
        return false;
    }


    @Override
    public void receiveTrackSoftware(String s) throws RemoteException {

    }

    @Override
    public String sendTrackSoftware() throws RemoteException {
        return null;
    }
*/

}