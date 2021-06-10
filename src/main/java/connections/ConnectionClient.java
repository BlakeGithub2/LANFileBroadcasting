package connections;

import connections.broadcast.BroadcastClient;
import connections.tcp.TCPClient;
import main.connectpage.ConnectPage;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectionClient {
    private BroadcastClient broadcastClient;
    private TCPClient tcpClient;

    private ConnectPage connectPage;
    private boolean connected;

    public ConnectionClient(ConnectPage page) {
        this.connectPage = page;
        broadcastClient = new BroadcastClient(page);
        tcpClient = new TCPClient();
    }

    public void searchForBroadcasts() throws IOException {
        broadcastClient.searchForBroadcasts();
    }

    public void connect(InetAddress ip) throws IOException {
        if (tcpClient.connect(ip)) {
            broadcastClient.stopSearchingForBroadcasts();
        }
    }

    public void disconnect() {
        tcpClient.disconnect();
    }
}
