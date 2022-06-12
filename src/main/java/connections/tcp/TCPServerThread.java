package connections.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerThread extends Thread {
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
        while (isAlive()) {
            try {
                Socket client = socket.accept();
                System.out.println("Client accepted.");
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();

        // Socket close code only works here
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
