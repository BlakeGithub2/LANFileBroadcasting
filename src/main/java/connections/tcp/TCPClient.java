package connections.tcp;

import connections.ThreadHandler;

import java.io.IOException;
import java.net.Inet4Address;

public class TCPClient extends ThreadHandler {
    private Inet4Address address;

    public TCPClient(Inet4Address address) {
        this.address = address;
    }

    @Override
    public Thread createThread() {
        try {
            return new TCPClientThread(address);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
