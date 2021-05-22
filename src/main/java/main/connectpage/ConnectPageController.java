package main.connectpage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

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
    private TilePane explorePane;

    @FXML
    private void triggerBroadcasting() {
        page.triggerBroadcasting();
        page.triggerButton(broadcastButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addModel(new ConnectPage(explorePane));
    }
}