package main.browse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import main.Main;
import utils.FileUtils;

import java.io.File;
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

    @FXML
    private void add() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Project Directory");
        File selectedDir = chooser.showDialog(Main.getSceneController().getMainWindow());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Import Project Confirmation");
        alert.setHeaderText("Import project " + selectedDir.getName() + "?");

        String contentText = "";
        contentText = contentText.concat("Size of project: " + FileUtils.getByteValueString(FileUtils.directorySize(selectedDir)) + "\n");
        contentText = contentText.concat("Remaining disk storage: " + FileUtils.getByteValueString(selectedDir.getUsableSpace()) + "\n");
        alert.setContentText(contentText);

        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}