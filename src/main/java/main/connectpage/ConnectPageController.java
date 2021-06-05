package main.connectpage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import main.Main;
import main.utils.ImageUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectPageController implements Initializable {
    private static ConnectPage page;

    public static void addModel(ConnectPage page) {
        ConnectPageController.page = page;
    }

    @FXML
    private Button broadcastButton;

    @FXML
    private Button connectButton;

    @FXML
    private ListView connectionList;

    @FXML
    private void triggerBroadcasting() {
        page.triggerBroadcasting();
        page.triggerButton(broadcastButton);
    }

    @FXML
    private void triggerConnect() {
        Connection selected = (Connection) connectionList.getSelectionModel().getSelectedItem();

        // Nothing is selected
        if (selected == null) {
            displayNothingSelectedMessage();
            return;
        }

        // Self is selected
        if (selected.getAddress() == null) {
            Main.getSceneController().activate("browse");
            return;
        }

        try {
            page.connect(selected.getAddress());
            Main.getSceneController().activate("browse");
        } catch (IOException e) {
            displayCouldNotConnectMessage();
            e.printStackTrace();
        }
    }
    private void displayCouldNotConnectMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not connect to the server.");
        a.show();
    }
    private void displayNothingSelectedMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Please select a server to connect to.");
        a.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addModel(new ConnectPage());
        connectionList.setItems(page.getConnections());
        connectionList.setCellFactory(param -> new ListCell<Connection>() {
            @Override
            protected void updateItem(Connection item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (item.getAddress() == null) {
                        setText("Your files");
                    } else {
                        // TODO: Temp fix, getHost() slow
                        setText(item.getAddress().toString());
                    }

                    ImageView graphic = ImageUtils.loadImageView("server.png");
                    graphic.setFitWidth(64);
                    graphic.setFitHeight(64);

                    setGraphic(graphic);
                }
            }
        });
    }
}