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
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BrowseFilePageController implements Initializable {
    private static BrowseFilePage page;

    @FXML
    private ListView projectList;

    @FXML
    private void goBack() {
        Main.getSceneController().activate("newconnect");
    }

    @FXML
    private void add() {
        File selectedDir = chooseDirectory();
        if (selectedDir != null) {
            showAddProjectConfirmation(selectedDir);
        }
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
            try {
                addProject(selectedDir);
            } catch (Exception e) {
                showNonexistentFileErrorMessage();
                e.printStackTrace();
            }
        }
    }
    private void showNonexistentFileErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Could not find the project file.");
        alert.show();
    }
    private void addProject(File selectedDir) throws Exception {
        // TODO: Copy project over so that local version control works

        page.addProject(selectedDir);
        trySave();
    }
    private void showCouldNotSavePageMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not save projects arrangement.");
        a.showAndWait();
    }
    private void trySave() {
        try {
            page.save();
        } catch (IOException e) {
            e.printStackTrace();
            showCouldNotSavePageMessage();
        }
    }

    @FXML
    private void delete() {
        Project toDelete = (Project) projectList.getSelectionModel().getSelectedItem();

        if (toDelete == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("No project selected for deletion.");
        } else {
            page.deleteProject(toDelete);
        }

        trySave();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        page = new BrowseFilePage();

        // Has to check if address file exists or program will not go to install
        // screen if file isn't found, due to program being in initialization phase
        if (Main.getBaseFile().addressFileExists()) {
            try {
                page.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        projectList.setItems(page.getProjects());
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

    public BrowseFilePage getPage() {
        return page;
    }
}