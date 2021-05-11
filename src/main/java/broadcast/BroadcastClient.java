package broadcast;

import main.ConnectPage;

import java.io.IOException;

public class BroadcastClient {
    private ConnectPage connectPage;

    public BroadcastClient(ConnectPage page) {
        this.connectPage = page;
    }

    public void searchForBroadcasts() throws IOException {
        new BroadcastClientThread(connectPage).start();
    }
}
