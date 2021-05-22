package main.connectpage;

import broadcast.BroadcastClient;
import broadcast.BroadcastServer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.Page;

import java.io.IOException;

public class ConnectPage implements Page {
    private ConnectionList connections;
    private BroadcastServer server;

    public ConnectPage(Pane explorePane) {
        createConnectionsList(explorePane);
        onCreation();
        server = new BroadcastServer();
    }

    private void createConnectionsList(Pane explorePane) {
        connections = new ConnectionList(explorePane);
    }

    private void onCreation() {
        connections.addConnection("Your Files", null);

        try {
            addConnections();
        } catch (IOException e) {
            System.out.println("Could not add connections. (ConnectPage.java)");
            e.printStackTrace();
        }
    }

    private void addConnections() throws IOException {
        BroadcastClient client = new BroadcastClient(this);
        client.searchForBroadcasts();
    }

    public void triggerBroadcasting() {
        // Toggle server broadcast
        try {
            server.toggle();
        } catch (IOException e) {
            System.out.println("Could not toggle broadcast.");

            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
            e.printStackTrace();
            return;
        }

    }
    public void triggerButton(Button broadcastButton) {
        if (server.isBroadcasting()) {
            broadcastButton.setText("Stop Broadcasting");
        } else {
            broadcastButton.setText("Start Broadcasting");
        }
    }

    // Getters
    public boolean isBroadcasting() {
        return server.isBroadcasting();
    }

    public ConnectionList getConnectionList() {
        return connections;
    }
}
