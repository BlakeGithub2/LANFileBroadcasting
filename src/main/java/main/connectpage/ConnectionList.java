package main.connectpage;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.net.InetAddress;
import java.util.ArrayList;

public class ConnectionList {
    // Variables
    private ArrayList<Connection> connections;
    private Pane pane;

    // Constructors
    public ConnectionList(Pane pane) {
        this.pane = pane;
        this.connections = new ArrayList<>();
    }

    // Misc.
    public void addConnection(String name, InetAddress ip) {
        Connection connection = new Connection(name, ip);

        if (!containsAddress(connection.getAddress())) {
            connections.add(connection);
            addToPane(connection);
        }
    }
    private void addToPane(Connection connection) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pane.getChildren().add(connection.getPane());
            }
        });
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

    // Getters
    public ArrayList<Connection> getConnections() {
        return connections;
    }

}
