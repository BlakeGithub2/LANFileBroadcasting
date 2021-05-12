package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LoginPage implements Page {
    private String name;
    private Scene scene;

    @Override
    public void loadScene() {

        Pane pane = new VBox();
        Label nameLabel = new Label("Name: ");
        TextField nameInput = new TextField();

        Pane nameBox = new HBox();
        nameBox.getChildren().add(nameLabel);
        nameBox.getChildren().add(nameInput);

        pane.getChildren().add(nameBox);

        try {
            Pane root = FXMLLoader.load(getClass().getResource("/scenes/login.fxml"));
            scene = new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        } catch (IOException e) {
            System.out.println("Exception loading login page scene. See LoginPage.java.");
            e.printStackTrace();
        }
    }

    @Override
    public Scene getScene() {
        if (scene == null) {
            loadScene();
        }
        return scene;
    }
}
