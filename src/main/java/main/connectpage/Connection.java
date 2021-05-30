package main.connectpage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.GUIPane;
import main.Main;
import main.utils.ImageUtils;

import java.net.InetAddress;

public class Connection implements GUIPane {
    private String name = "";
    private InetAddress ip;

    public Connection(String name, InetAddress ip) {
        this.name = name;
        this.ip = ip;
    }

    @Override
    public HBox getPane() {
        HBox pane = new HBox();
        VBox nameAndButton = new VBox();

        // Create image
        ImageView fileImageView = ImageUtils.loadImageView("folder.png");
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

        // TODO: Move to a Controller class?
        if (ip == null) {
            connectButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.getSceneController().activate("browse");
                }
            });
        }

        nameAndButton.getChildren().add(connectButton);
        nameAndButton.setSpacing(5);
        nameAndButton.setAlignment(Pos.CENTER);

        return pane;
    }

    public InetAddress getAddress() {
        return ip;
    }
}
