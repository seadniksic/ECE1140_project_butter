package sample;

import javafx.scene.layout.GridPane;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Track_Model_Interface extends Remote {

//    public void send_Distance(double distance){ }

    public void spawn_Train_In_Yard(int param_Line_Index, int param_Block_Number, GridPane param_Gridpane) throws RemoteException;

    //                                  Line                    train number           distance traveled from last tick
    public void update_Occupancy(GridPane param_Gridpane, int param_Line_Index, int param_Occupancy_Index, Double param_Distance_Traveled_In_Tick) throws RemoteException;

    //                                                                              switch state
    public void set_Switch_At_Block(int param_Line_Index, int param_Block_Number, Boolean param_Is_Switched) throws RemoteException;
    public void set_Light_At_Block(int param_Line_Index, int param_Block_Number, Boolean param_Is_Switched) throws RemoteException;
    public void set_Crossbar_At_Block(int param_Line_Index, int param_Block_Number, Boolean param_Is_Switched) throws RemoteException;

    // ??
    //public void send_Speed_Authority(int train_Num, int speed, int authority, double grade);


}