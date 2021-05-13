package main.connectpage;

import broadcast.BroadcastClient;
import broadcast.BroadcastServer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.Main;
import main.Page;

import java.io.IOException;

public class ConnectPage implements Page {
    private Scene scene;
    private ConnectionList connections;
    private BroadcastServer server;

    public ConnectPage() {
        loadScene();
        createConnectionsList();
        onCreation();
        server = new BroadcastServer();
        System.out.println("Connect page created.");
    }

    private void createConnectionsList() {
        Pane explorePane = (Pane) scene.lookup("#explore_pane");
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
            e.printStackTrace();
            return;
        }

        // Switch button text
        Button broadcastButton = (Button) scene.lookup("#broadcast_button");
        if (server.isBroadcasting()) {
            broadcastButton.setText("Stop Broadcasting");
        } else {
            broadcastButton.setText("Start Broadcasting");
        }
    }

    @Override
    public void loadScene() {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/scenes/connectcode.fxml"));
            scene = new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        } catch (IOException e) {
            System.out.println("Exception loading login page scene. See ConnectPage.java.");
            e.printStackTrace();
            return;
        }
    }

    // Getters

    @Override
    public Scene getScene() {
        return scene;
    }

    public boolean isBroadcasting() {
        return server.isBroadcasting();
    }

    public ConnectionList getConnectionList() {
        return connections;
    }
}
