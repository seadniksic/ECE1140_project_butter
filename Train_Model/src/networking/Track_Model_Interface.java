package networking;

import java.rmi.Remote;

public interface Track_Model_Interface extends Remote {

    public void send_Distance(double distance);

}
