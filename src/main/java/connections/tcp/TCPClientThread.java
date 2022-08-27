package connections.tcp;

import connections.tcp.instructions.distribution.InstructionReceiver;
import connections.tcp.instructions.distribution.InstructionSender;

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
    private InstructionSender sender;
    private InstructionReceiver receiver;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public TCPClientThread(InetAddress host) throws IOException {
        this("TCPClientThread", host);
    }

    public TCPClientThread(String name, InetAddress host) throws IOException {
        super(name);
        socket = new Socket(host, 4447);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        receiver = new InstructionReceiver(in);
        sender = new InstructionSender(out);
    }

    public void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("connected", l);
        pcs.addPropertyChangeListener("add-downloadable-project", l);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                receiver.executeInstructionFromSocket(sender);
            } catch (IOException e) {
                notifyDisconnected();
                break;
            }
        }
        try {
            socket.getOutputStream().close();
        } catch (IOException e) {
            notifyDisconnected();
        }
    }

    public void addNetworkData(String name, Object obj) {
        sender.addNetworkData(name, obj);
    }


    public void notifyDisconnected() {
        pcs.firePropertyChange("connected", true, false);
    }

    public long sendInstruction(String instruction) {
        try {
            return sender.send(instruction);
        } catch (IOException e) {
            notifyDisconnected();
        }

        return -1;
    }
}
