package connections;

import java.io.IOException;

public abstract class ConnectionThread extends Thread {
    protected boolean shouldStop;

    public ConnectionThread(String name) throws IOException {
        super(name);
    }

    public void stopConnection() {
        shouldStop = true;
        interrupt();
    }
}
