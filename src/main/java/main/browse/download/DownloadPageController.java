package main.browse.download;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import main.Main;
import main.browse.Project;
import main.utils.ImageUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class DownloadPageController implements Initializable {
    private static DownloadPage page;

    @FXML
    private ListView projectList;

    @FXML
    public void download() {
        System.out.println("preparing to download...");
    }

    @FXML
    public void goBack() {
        Main.getSceneController().activate("connect");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        page = new DownloadPage();

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
}