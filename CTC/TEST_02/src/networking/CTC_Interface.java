package networking;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CTC_Interface extends Remote{

    public void close_Line(String line) throws RemoteException;//close entire track so each block is closed

    public void close_Section(String line, char section) throws RemoteException;//close all blocks in section

    public void close_Blocks(String line, char section, int startBlock, int endBlock) throws RemoteException;//close all blocks from start to stop

    public void close_Block(String line, int block) throws RemoteException;

    public void open_Line(String line) throws RemoteException;//close entire track so each block is closed

    public void open_Section(String line, char section) throws RemoteException;//close all blocks in section

    public void open_Blocks(String line, char section, int startBlock, int endBlock) throws RemoteException;//close all blocks from start to stop

    public void open_Block(String line, int block) throws RemoteException;

    public void train_Moved(int trainNum, int block) throws RemoteException, InterruptedException;//moves occupancy state of block of train

    public void change_Lights(String line, int block, boolean state) throws RemoteException;

    public void change_CrossBar(String line, int block, boolean state) throws RemoteException;


    //public void import_Track_File(File trackFile) throws FileNotFoundException, RemoteException; // receives file from track model NOT IMPLEMENTED
    public void add_Ticket_CTC(int trainNum) throws RemoteException;

    public void add_Tickets(int trainNum, int numTickets) throws RemoteException;


    public void update_Time(double time) throws RemoteException;//time of units in seconds
}