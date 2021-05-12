package main.connectpage;

import broadcast.BroadcastClient;
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
    private static boolean broadcasting;
    private Scene scene;

    public ConnectPage() {
        loadScene();
        onCreation();
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
        // Toggle broadcasting status
        broadcasting = !broadcasting;

        // Switch button text
        Button broadcastButton = (Button) scene.lookup("#broadcast_button");
        if (broadcasting) {
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
        return broadcasting;
    }
}
