package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ConnectPage implements Page {
    private String name;

    @Override
    public Scene getScene() {
        Scene scene = null;

        try {
            Pane root = FXMLLoader.load(getClass().getResource("/scenes/connectcode.fxml"));
            scene = new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        } catch (IOException e) {
            System.out.println("Exception loading login page scene. See ConnectPage.java.");
            e.printStackTrace();
            return null;
        }

        Connection selfConnection = new Connection("Your Files");
        addToExplorePane(scene, selfConnection.getPane());

        return scene;
    }

    private void addToExplorePane(Scene scene, Pane pane) {
        Pane explorePane = (Pane) scene.lookup("#explore_pane");
        explorePane.getChildren().add(pane);
    }
}
