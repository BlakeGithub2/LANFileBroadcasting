package connections.broadcast;

import connections.exceptions.AlreadyActivatedException;
import main.connectpage.ConnectPage;

import java.io.IOException;

public class BroadcastClient {
    private ConnectPage connectPage;
    private BroadcastClientThread broadcastThread;
    private boolean searching;

    public BroadcastClient(ConnectPage page) {
        this.connectPage = page;
    }

    public void searchForBroadcasts() throws IOException {
        if (!searching) {
            broadcastThread = new BroadcastClientThread(connectPage);
            broadcastThread.start();
            searching = true;
        } else {
            throw new AlreadyActivatedException("Already searching for broadcasts.");
        }
    }

    public void stopSearchingForBroadcasts() {
        if (searching) {
            broadcastThread.stopConnection();
            searching = false;
        } else {
            throw new AlreadyActivatedException("Already not searching for broadcasts.");
        }
    }
}
