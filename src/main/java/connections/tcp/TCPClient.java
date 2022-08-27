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

    public long sendInstruction(String instruction) throws IOException, ClassNotFoundException {
        return clientThread.sendInstruction(instruction);
    }

    public void addNetworkData(String name, Object obj) {
        clientThread.addNetworkData(name, obj);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        stop();
    }
}
