package connections.tcp;

import connections.tcp.instructions.InstructionReceiver;
import connections.tcp.instructions.InstructionSender;

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
                InstructionReceiver.readInstructionFromSocket(out, in);
            } catch (IOException e) {
                break;
            }
        }
        try {
            socket.getOutputStream().close();
        } catch (IOException e) {
            notifyDisconnected();
        }
    }


    public void notifyDisconnected() {
        pcs.firePropertyChange("connected", true, false);
    }

    public Object sendInstruction(String instruction) {
        try {
            return InstructionSender.sendInstruction(out, in, instruction);
        } catch (IOException e) {
            e.printStackTrace();
            notifyDisconnected();
        }

        return null;
    }
}
