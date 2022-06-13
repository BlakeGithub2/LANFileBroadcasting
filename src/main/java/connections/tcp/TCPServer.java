package connections.tcp;

import connections.ThreadHandler;

import java.io.IOException;

public class TCPServer extends ThreadHandler {
    @Override
    public Thread createThread() {
        try {
            return new TCPServerThread();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
