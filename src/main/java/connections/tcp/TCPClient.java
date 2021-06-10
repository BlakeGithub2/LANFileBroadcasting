package connections.tcp;

import connections.exceptions.AlreadyActivatedException;

import java.io.IOException;
import java.net.InetAddress;

public class TCPClient {
    private TCPClientThread tcpThread;
    private boolean connected;

    public boolean connect(InetAddress ip) throws IOException {
        if (!connected) {
            tcpThread = new TCPClientThread(ip);
            tcpThread.start();
            connected = true;
            return true;
        }
        return false;
    }

    public void disconnect() {
        if (connected) {
            connected = false;
            tcpThread.stopConnection();
        } else {
            throw new AlreadyActivatedException("Already connected to nothing.");
        }
    }
}
