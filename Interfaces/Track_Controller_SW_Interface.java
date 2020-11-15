package networking;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Track_Controller_SW_Interface extends Remote {

    //Track Model calls these methods
    void make_Controllers(String[] lines, int[][] blocks) throws RemoteException;

    void train_Moved(int trainNum, int blockNum) throws RemoteException, FileNotFoundException; //Track Model -> Track Controller -> CTC
    //Track Model calls these methods

    //CTC calls these methods
    void create_Train(int cars, String line, int block) throws RemoteException; // CTC -> track controller -> track model -> train model

    void send_Speed_Authority(int train_Num, double speed, int authority) throws RemoteException; // CTC -> track controller -> track model -> train model

    boolean set_Switch_Manual(String trackLine, int blockNum) throws RemoteException;

    //static void add_Ticket(int trainNum) throws RemoteException;

    void close_Block(String trackLine, int blockNum) throws RemoteException;

    void open_Block(String trackLine, int blockNum) throws RemoteException, FileNotFoundException;
    //CTC calls these methods

    //Hardware calls these methods
    void get_Block_Array(int start) throws RemoteException;
    //Hardware calls these methods

    //public void send_Block_State(int blockNum, boolean state) throws RemoteException;
    //Hardware calls these methods

    void update_Time(double time) throws RemoteException;
}
