package main;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

public class Connection {
    private String name = "";

    public Connection(String name) {
        this.name = name;
    }

    public HBox getPane() {
        HBox pane = new HBox();
        VBox nameAndButton = new VBox();

        // Create image
        FileInputStream fileImage = null;
        try {
            URL url = getClass().getResource("/sprites/folder.png");
            fileImage = new FileInputStream(url.getFile());
        } catch (FileNotFoundException e) {
            System.out.println("Folder image not found. Connection.java");
        }

        ImageView fileImageView = new ImageView(new Image(fileImage));
        fileImageView.setFitWidth(100);
        fileImageView.setFitHeight(100);

        // Create HBox
        pane.getChildren().add(fileImageView);
        pane.getChildren().add(nameAndButton);
        pane.setSpacing(10);
        pane.setAlignment(Pos.CENTER);

        // Create text to display computer name
        Text nameText = new Text(name);
        nameText.setFont(new Font(15));
        nameAndButton.getChildren().add(nameText);

        // Create VBox within HBox
        Button connectButton = new Button("Connect");
        nameAndButton.getChildren().add(connectButton);
        nameAndButton.setSpacing(5);
        nameAndButton.setAlignment(Pos.CENTER);

        return pane;
    }
}
