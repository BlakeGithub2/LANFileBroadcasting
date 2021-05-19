package main.install;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import main.Main;

import java.io.File;

public class InstallPageController {
    private static InstallPage page;

    @FXML
    private TextField selectFolderBox;

    public static void addModel(InstallPage page) {
        InstallPageController.page = page;
    }

    @FXML
    private void selectFolder() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select Install Folder");
        File selectedDir = dirChooser.showDialog(Main.getSceneController().getMainWindow());

        if (selectedDir == null) {
            return;
        }

        selectFolderBox.setText(selectedDir.getAbsolutePath());
    }

    @FXML
    private void submit() {
        // Return if text box does not contain text
        if (selectFolderBox.getText().equals("")) {
            showNoTextFailureAlert();
            return;
        }

        String dirStr = selectFolderBox.getText().trim() + "/" + Main.MAIN_FILE_NAME;
        File file = new File(dirStr);

        boolean createdFile = file.mkdir();

        // File could not be created
        if (createdFile) {
            Main.getSceneController().activate("connectcode");
        } else {
            showCreateFileFailureAlert();
            return;
        }

        // Save file
        Main.getAddressFile().saveBasePath(dirStr);
    }

    private void showNoTextFailureAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("No save path inputted.");
        a.show();
    }
    private void showCreateFileFailureAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not create folder. Check to ensure a folder can be created at the inputted " +
                "path, no folder or file with the same name exists, " +
                "and you have proper permissions.");
        a.show();
    }
}
