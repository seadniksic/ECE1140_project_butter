package sample;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Track_Controller_SW_Interface extends Remote {

    //Track Model calls these methods
    public void make_Controllers(String[] lines, int[][] blocks) throws RemoteException;

    public void train_Moved(int trainNum, int blockNum) throws RemoteException, FileNotFoundException; //Track Model -> Track Controller -> CTC
    //Track Model calls these methods

    //CTC calls these methods
    public void create_Train(int cars) throws RemoteException; // CTC -> track controller -> track model -> train model

    public void send_Speed_Authority(int train_Num, double speed, int authority) throws RemoteException; // CTC -> track controller -> track model -> train model

    public void set_Switch_Manual(String trackLine, int blockNum, boolean prevSwitchPos) throws RemoteException;

    public int get_Ticket_Sales() throws RemoteException;

    public void close_Block(String trackLine, int blockNum) throws RemoteException;

    public void open_Block(String trackLine, int blockNum) throws RemoteException, FileNotFoundException;
    //CTC calls these methods

    //Hardware calls these methods
    public void get_Block_Array(int start) throws RemoteException;

    public void send_Block_State(int blockNum, boolean state) throws RemoteException;
    //Hardware calls these methods

    public void update_Time(double time) throws RemoteException;
}
