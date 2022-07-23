package connections;

import java.io.IOException;

public abstract class UDPThreadHandler {
    private Thread thread;
    private boolean active;

    public void toggle() throws IOException {
        if (!active) {
            start();
        } else {
            stop();
        }
    }
    public void start() throws IOException {
        if (!active) {
            thread = createThread();
            if (!canSuccessfullyConnect()) {
                throw new IOException("UDP client unable to successfully connect.");
            }
            thread.start();
            active = true;
        } else {
            System.out.println("UDP Thread handler: Thread already active! " + getName());
        }
    }
    public void stop() {
        if (active) {
            thread.interrupt();
            active = false;
        } else {
            System.out.println("UDP Thread handler: Cannot stop an inactive thread! " + getName());
        }
    }

    public boolean canSuccessfullyConnect() {
        return thread != null;
    }

    public abstract Thread createThread() throws IOException;
    public abstract String getName();
    public boolean isActive() {
        return active;
    }
}
