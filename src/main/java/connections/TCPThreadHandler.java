package connections;

import java.io.IOException;
import java.net.InetAddress;

public abstract class TCPThreadHandler {
    private Thread thread;
    private boolean active;

    public void start(InetAddress address) throws IOException {
        if (!active) {
            thread = createThread(address);

            if (!canSuccessfullyConnect()) {
                throw new IOException("TCP client unable to successfully connect.");
            }
            thread.start();
            active = true;
        } else {
            System.out.println("TCP Thread handler: Thread already active! " + getName());
        }
    }
    public void stop() {
        if (active) {
            thread.interrupt();
            active = false;
        } else {
            System.out.println("TCP Thread handler: Cannot stop an inactive thread! " + getName());
        }
    }

    public boolean canSuccessfullyConnect() {
        return thread != null;
    }

    public abstract Thread createThread(InetAddress address) throws IOException;
    public abstract String getName();
    public boolean isActive() {
        return active;
    }
}
