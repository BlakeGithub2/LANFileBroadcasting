package connections.tcp;

import connections.UDPThreadHandler;

public class TCPServer extends UDPThreadHandler {
    @Override
    public Thread createThread() {
        return new TCPMultiServer();
    }

    @Override
    public String getName() {
        return "TCPServer";
    }
}
