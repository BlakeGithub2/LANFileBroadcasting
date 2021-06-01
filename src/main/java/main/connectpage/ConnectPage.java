package main.connectpage;

import broadcast.BroadcastClient;
import broadcast.BroadcastServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import main.Page;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectPage implements Page {
    private BroadcastServer server;

    private ObservableList<Connection> connections = FXCollections.observableArrayList();

    public ConnectPage() {
        onCreation();
        server = new BroadcastServer();
    }

    private void onCreation() {
        addConnection("Your Files", null);

        try {
            addConnections();
        } catch (IOException e) {
            System.out.println("Could not add connections. (ConnectPage.java)");
            e.printStackTrace();
        }
    }

    public boolean addConnection(String name, InetAddress ip) {
        Connection connection = new Connection(name, ip);

        if (!containsAddress(connection.getAddress())) {
            connections.add(connection);
            return true;
        }

        return false;
    }
    public boolean containsAddress(InetAddress ip) {
        for (Connection c : connections) {
            if (c.getAddress() == null) {
                continue;
            }
            if (c.getAddress().equals(ip)) {
                return true;
            }
        }
        return false;
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

    public ObservableList<Connection> getConnections() {
        return connections;
    }
}
