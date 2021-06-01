package main.connectpage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import main.utils.ImageUtils;

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
    private ListView connectionList;

    @FXML
    private void triggerBroadcasting() {
        page.triggerBroadcasting();
        page.triggerButton(broadcastButton);
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
                    setText("connection");

                    ImageView graphic = ImageUtils.loadImageView("server.png");
                    graphic.setFitWidth(64);
                    graphic.setFitHeight(64);

                    setGraphic(graphic);
                }
            }
        });
    }
}