package main.browse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import main.Main;
import utils.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Optional;
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
        File selectedDir = chooseDirectory();
        showAddProjectConfirmation(selectedDir);
    }
    private File chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Project Directory");
        File selectedDir = chooser.showDialog(Main.getSceneController().getMainWindow());

        return selectedDir;
    }
    private void showAddProjectConfirmation(File selectedDir) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Import Project Confirmation");
        alert.setHeaderText("Import project " + selectedDir.getName() + "?");

        String contentText = "";
        contentText = contentText.concat("Size of project: " + FileUtils.getByteValueString(FileUtils.directorySize(selectedDir)) + "\n");
        contentText = contentText.concat("Remaining disk storage: " + FileUtils.getByteValueString(selectedDir.getUsableSpace()) + "\n");
        alert.setContentText(contentText);

        createAddProjectConfirmationAlertButtons(alert);
    }
    private void createAddProjectConfirmationAlertButtons(Alert alert) {
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            addProject();
        } else {
            cancelAddProject();
        }
    }
    private void addProject() {

    }
    private void cancelAddProject() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}