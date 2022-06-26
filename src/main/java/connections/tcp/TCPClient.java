package connections.tcp;

import connections.ThreadHandler;

import java.io.IOException;
import java.net.InetAddress;

public class TCPClient extends ThreadHandler {
    private InetAddress address;
    private TCPClientThread clientThread;

    public TCPClient(InetAddress address) {
        this.address = address;
    }

    @Override
    public Thread createThread() {
        try {
            this.clientThread = new TCPClientThread(address);
            return this.clientThread;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object sendInstruction(String instruction) throws IOException, ClassNotFoundException {
        return clientThread.sendInstruction(instruction);
    }
}
