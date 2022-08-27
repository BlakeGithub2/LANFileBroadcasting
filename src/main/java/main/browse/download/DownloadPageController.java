package main.browse.download;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import main.IController;
import main.utils.ImageUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DownloadPageController implements IController {
    private static DownloadPage page;

    @FXML
    private ListView projectList;

    @FXML
    public void download() {
        page.downloadProject((String) projectList.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void goBack() {
        try {
            page.exit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        page = new DownloadPage();

        projectList.setItems(page.getProjects());
        projectList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);

                            ImageView graphic = ImageUtils.loadImageView("folder.png");
                            graphic.setFitWidth(64);
                            graphic.setFitHeight(64);

                            setGraphic(graphic);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLoad() {
        page.onLoad();
    }
}