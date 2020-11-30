package networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Track_Controller_HW_Interface extends Remote {

    //public void receiveTrackSoftware(String s) throws RemoteException;
    //public String sendTrackSoftware() throws RemoteException;

    // this function will tell the hardware what Line and block it's on and if it has an attribute
    // Attributes can be crossing, station, etc

    public void set_Block_State(int BLockNum, boolean state) throws RemoteException;

    //public boolean lightStatus() throws RemoteException;
    //public boolean barrierStatus() throws RemoteException;
    //public boolean switchPosition() throws RemoteException;

    //public void sendSpeed(int train, int speed) throws RemoteException;
    //public void sendAuthority(int train, int Authority) throws RemoteException;
    //public void dispatchTrain() throws RemoteException;
    //public void reprogramPLC (String Line, int Block, String Attribute) throws RemoteException;

}