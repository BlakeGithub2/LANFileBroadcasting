package connections.broadcast;

import connections.ThreadHandler;

import java.io.IOException;

public class BroadcastServer extends ThreadHandler {
    @Override
    public Thread createThread() {
        try {
            return new BroadcastServerThread();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
