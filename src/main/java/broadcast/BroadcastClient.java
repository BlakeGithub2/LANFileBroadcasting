package broadcast;

import java.io.IOException;

public class BroadcastClient {
    public void searchForBroadcasts() throws IOException {
        new BroadcastClientThread().start();
    }
}
