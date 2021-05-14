package main.browse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseFilePageController implements Initializable {
    private static BrowseFilePage page;

    public BrowseFilePageController() { }

    public static void addModel(BrowseFilePage page) {
        BrowseFilePageController.page = page;
    }

    @FXML
    private void goBack() {
        Main.getSceneController().activate("connectcode");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}