package main.connectpage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectPageController implements Initializable {
    private static ConnectPage page;

    public ConnectPageController() { }

    public static void goTo(Stage stage) {
        stage.setScene(page.getScene());
        stage.show();
    }

    public static void addModel(ConnectPage page) {
        ConnectPageController.page = page;
    }


    @FXML
    private void triggerBroadcasting() {
        page.triggerBroadcasting();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}