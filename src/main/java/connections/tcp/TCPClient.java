package connections.tcp;

import connections.TCPThreadHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;

public class TCPClient extends TCPThreadHandler implements PropertyChangeListener {
    private TCPClientThread clientThread;

    @Override
    public Thread createThread(InetAddress address) throws IOException {
        clientThread = new TCPClientThread(address);
        return clientThread;
    }

    public void addObserver(PropertyChangeListener l) {
        clientThread.addObserver(l);
    }

    @Override
    public String getName() {
        return "TCPClient";
    }

    public void sendInstruction(String instruction) throws IOException, ClassNotFoundException {
        clientThread.sendInstruction(instruction);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        stop();
    }
}
