package connections.tcp;

import connections.ConnectionThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerThread extends ConnectionThread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU
    private ServerSocket socket;

    public TCPServerThread() throws IOException {
        this("TCPServerThread");
    }

    public TCPServerThread(String name) throws IOException {
        super(name);
        socket = new ServerSocket(4447);
    }

    @Override
    public void run() {
        while (!shouldStop) {
            try {
                Socket client = socket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
