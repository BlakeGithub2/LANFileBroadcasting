package connections.tcp;

import connections.exceptions.AlreadyActivatedException;
import main.connectpage.ConnectPage;

import java.io.IOException;
import java.net.InetAddress;

public class TCPClient {
    private ConnectPage connectPage;
    private TCPClientThread tcpThread;
    private boolean connected;

    public TCPClient(ConnectPage page) {
        this.connectPage = page;
    }

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
