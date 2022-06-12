package main.connectpage;

import connections.broadcast.BroadcastClientThread;
import connections.broadcast.BroadcastServer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import main.Page;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectPage implements Page {
    private BroadcastServer server;
    private BroadcastClientThread client;
    private boolean broadcasting;

    private ObservableList<Connection> connections = FXCollections.observableArrayList();

    public ConnectPage() {
        try {
            server = new BroadcastServer();
            client = new BroadcastClientThread(this);
            broadcasting = false;
            onCreation();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    connections.add(connection);
                }
            });

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
        client.start();
    }

    public void triggerBroadcasting() {
        server.toggle();
        broadcasting = !broadcasting;
    }
    public void triggerButton(Button broadcastButton) {
        if (server.isActive()) {
            broadcastButton.setText("Stop Broadcasting");
        } else {
            broadcastButton.setText("Start Broadcasting");
        }
    }

    // Getters
    public ObservableList<Connection> getConnections() {
        return connections;
    }
}
