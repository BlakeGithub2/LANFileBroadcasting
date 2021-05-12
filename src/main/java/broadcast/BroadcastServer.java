package broadcast;

import java.io.IOException;

public class BroadcastServer {
    private BroadcastServerThread thread;
    private boolean broadcasting;

    public void toggle() throws IOException {
        if (!broadcasting) {
            thread = new BroadcastServerThread();
            thread.start();
        } else {
            thread.interrupt();
        }
        broadcasting = !broadcasting;
    }

    public boolean isBroadcasting() {
        return broadcasting;
    }
}
