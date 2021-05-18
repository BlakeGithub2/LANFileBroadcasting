package main.install;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InstallPageController {
    private static InstallPage page;

    @FXML
    private TextField selectFolderBox;

    @FXML
    private Button selectFolderButton;

    public static void addModel(InstallPage page) {
        InstallPageController.page = page;
    }

    @FXML
    private void submit() {

    }
}
