package connections.broadcast;

import connections.UDPThreadHandler;

import java.io.IOException;

public class BroadcastServer extends UDPThreadHandler {
    @Override
    public String getName() {
        return "BroadcastServer";
    }

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
