package connections;

import connections.broadcast.BroadcastServerThread;
import connections.tcp.TCPServerThread;

import java.io.IOException;

public class ConnectionServer {
    private BroadcastServerThread broadcastThread;
    private TCPServerThread tcpThread;
    private boolean broadcasting;

    public void toggle() throws IOException {
        if (!broadcasting) {
            broadcastThread = new BroadcastServerThread();
            broadcastThread.start();

            tcpThread = new TCPServerThread();
            tcpThread.start();
        } else {
            broadcastThread.stopConnection();
            tcpThread.stopConnection();
        }
        broadcasting = !broadcasting;
    }

    public boolean isBroadcasting() {
        return broadcasting;
    }
}
