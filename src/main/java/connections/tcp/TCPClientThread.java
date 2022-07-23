package connections.tcp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public TCPClientThread(InetAddress host) throws IOException {
        this("TCPClientThread", host);
    }

    public TCPClientThread(String name, InetAddress host) throws IOException {
        super(name);
        socket = new Socket(host, 4447);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("connected", l);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            notifyDisconnected();
        }
    }

    public Object sendInstruction(String instruction) throws IOException, ClassNotFoundException {
        try {
            out.writeUTF(instruction);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();
        return in.readObject();
    }

    public void notifyDisconnected() {
        pcs.firePropertyChange("connected", true, false);
    }
}
