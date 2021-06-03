package connections.tcp;

import connections.ConnectionThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClientThread extends ConnectionThread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU
    private Socket socket;

    public TCPClientThread(InetAddress host) throws IOException {
        this("TCPClientThread", host);
    }

    public TCPClientThread(String name, InetAddress host) throws IOException {
        super(name);
        socket = new Socket(host, 4447);
    }

    @Override
    public void run() {
        while (!shouldStop) {

        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
