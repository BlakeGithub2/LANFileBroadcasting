package connections.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClientThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public TCPClientThread(InetAddress host) throws IOException {
        this("TCPClientThread", host);
    }

    public TCPClientThread(String name, InetAddress host) throws IOException {
        super(name);
        socket = new Socket(host, 4447);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /*
    TODO: Find a better way to separate protocol from clients
     */
    @Override
    public void run() {
        while (isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object sendInstruction(String instruction) throws IOException, ClassNotFoundException {
        out.writeUTF(instruction);
        out.flush();
        return in.readObject();
    }
}
