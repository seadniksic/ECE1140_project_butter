package networking;

import java.rmi.Remote;

public interface Track_Model_Interface extends Remote {

    public String appendToHolder(String paramString);
    public void sendToSead();
}
