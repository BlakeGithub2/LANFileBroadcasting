package main.connectpage;

import connections.broadcast.BroadcastClientThread;
import connections.broadcast.BroadcastServer;
import connections.tcp.TCPClient;
import connections.tcp.TCPServer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import main.Main;
import main.Page;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;

public class ConnectPage implements Page, PropertyChangeListener {
    private BroadcastServer broadcastServer;
    private BroadcastClientThread broadcastClient;
    private TCPClient targetClient;
    private TCPServer targetServer;
    private boolean broadcasting;

    private ObservableList<Connection> connections = FXCollections.observableArrayList();

    public ConnectPage() {
        try {
            broadcastServer = new BroadcastServer();
            broadcastClient = new BroadcastClientThread();
            targetServer = new TCPServer();
            targetClient = new TCPClient();
            broadcastClient.addListener(this);
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
        broadcastClient.start();
    }

    public void triggerBroadcasting() throws IOException {
        broadcastServer.toggle();
        targetServer.toggle();

        broadcasting = !broadcasting;
    }
    public void triggerButton(Button broadcastButton) {
        if (broadcastServer.isActive()) {
            broadcastButton.setText("Stop Broadcasting");
        } else {
            broadcastButton.setText("Start Broadcasting");
        }
    }

    public void connectTo(InetAddress address) throws IOException {
        targetClient.start(address);
        Main.getSceneController().addDataToTransfer("tcpClient", targetClient);
    }

    // Getters
    public ObservableList<Connection> getConnections() {
        return connections;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (evt.getPropertyName().equals("connection") && (evt.getNewValue() != null)) {
                    Connection newConnection = broadcastClient.getNewConnection();
                    if (!containsAddress(newConnection.getAddress())) {
                        connections.add(newConnection);
                    }
                }
            }
        });
    }
}
