package networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Track_Controller_SW_Interface extends Remote {

    public void make_Controllers(String[] lines, int[][] blocks) throws RemoteException;

    public void create_Train(int cars) throws RemoteException; // (CTC -> track controller ->) track model -> train model

    public void send_Speed_Authority(int train_Num, int speed, int authority) throws RemoteException; // (CTC -> track controller ->) track model -> train model

    public void train_Moved(int trainNum, int blockNum) throws RemoteException; //Track Model -> Track Controller -> CTC

    public boolean get_Block_State() throws RemoteException;

    public boolean get_Switch() throws RemoteException;

    public boolean get_XBar_State() throws  RemoteException;

    public boolean get_Lights() throws RemoteException;

    public void set_Switch_Manual(int trackLine, int blockNum, boolean prevSwitchPos) throws RemoteException;

    public void set_XBar_State() throws RemoteException;

    public void set_Lights() throws RemoteException;

}
