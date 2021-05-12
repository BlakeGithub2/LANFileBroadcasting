package main.connectpage;

import broadcast.BroadcastClient;
import broadcast.BroadcastServer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.Connection;
import main.Main;
import main.Page;

import java.io.IOException;

public class ConnectPage implements Page {
    private Scene scene;
    private BroadcastServer server;

    public ConnectPage() {
        loadScene();
        onCreation();
        server = new BroadcastServer();
        System.out.println("Connect page created.");
    }

    private void addConnections() throws IOException {
        BroadcastClient client = new BroadcastClient(this);
        client.searchForBroadcasts();
    }

    public void addConnection(String name) {
        Connection selfConnection = new Connection(name);
        addToExplorePane(selfConnection.getPane());
    }

    private void addToExplorePane(Pane pane) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Pane explorePane = (Pane) scene.lookup("#explore_pane");
                explorePane.getChildren().add(pane);
                System.out.println("Connection added.");
            }
        });
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

    private void onCreation() {
        addConnection("Your Files");

        try {
            addConnections();
        } catch (IOException e) {
            System.out.println("Could not add connections. (ConnectPage.java)");
            e.printStackTrace();
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
}
