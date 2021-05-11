package main;

import broadcast.BroadcastClient;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ConnectPage implements Page {
    private String name;
    private boolean broadcasting;
    private Scene scene;

    private void addConnections() throws IOException {
        BroadcastClient client = new BroadcastClient(this);
        client.searchForBroadcasts();
    }

    public void addConnection(String name) {
        System.out.println("Adding connection...");
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

    @Override
    public void createScene() {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/scenes/connectcode.fxml"));
            scene = new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        } catch (IOException e) {
            System.out.println("Exception loading login page scene. See ConnectPage.java.");
            e.printStackTrace();
            return;
        }

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
        if (scene == null) {
            createScene();
        }

        return scene;
    }

    public boolean isBroadcasting() {
        return broadcasting;
    }
}
