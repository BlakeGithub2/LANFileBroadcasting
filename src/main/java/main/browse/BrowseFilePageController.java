package main.browse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import main.Main;
import utils.FileUtils;
import utils.ImageUtils;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BrowseFilePageController implements Initializable {
    private static BrowseFilePage page;

    public static void addModel(BrowseFilePage page) {
        BrowseFilePageController.page = page;
    }

    @FXML
    private ListView projectList;

    @FXML
    private void goBack() {
        Main.getSceneController().activate("connectcode");
    }

    @FXML
    private void add() {
        projectList.setItems(page.getProjects());
        File selectedDir = chooseDirectory();
        if (selectedDir != null) {
            showAddProjectConfirmation(selectedDir);
        }
        projectList.setCellFactory(param -> new ListCell<Project>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());

                    ImageView graphic = ImageUtils.loadImageView("folder.png");
                    graphic.setFitWidth(64);
                    graphic.setFitHeight(64);

                    setGraphic(graphic);
                }
            }
        });
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

        createAddProjectConfirmationAlertButtons(selectedDir, alert);
    }
    private void createAddProjectConfirmationAlertButtons(File selectedDir, Alert alert) {
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            addProject(selectedDir);
        }
    }
    private void addProject(File selectedDir) {
        // TODO: Copy project over so that local version control works

        // Add project directory to list
        page.addProject(selectedDir);
    }

    @FXML
    private void delete() {
        Project toDelete = (Project) projectList.getSelectionModel().getSelectedItem();
        if (toDelete == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("No project selected for deletion.");
        } else {
            // Delete the project
            projectList.getItems().remove(toDelete);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}