package broadcast;

import java.io.IOException;

public class BroadcastServer {
    public static void main(String[] args) throws IOException {
        new BroadcastServerThread().start();
    }
}
