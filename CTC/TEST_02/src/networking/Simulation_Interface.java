package networking;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Simulation_Interface extends Remote {

    void start_Simulation_Time(int multiplier) throws RemoteException, InterruptedException;
    void update_Multiplier(int multiplier) throws RemoteException;
    void pause_Simulation() throws RemoteException;
    void resume_Simulation() throws RemoteException;
    void reset_Simulation() throws RemoteException;
}

