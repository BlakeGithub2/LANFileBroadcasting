package main.browse;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.GUIPane;
import utils.ImageUtils;

import java.nio.file.Path;

public class Project implements GUIPane {
    private Path filepath;

    public Project(Path filepath) {
        this.filepath = filepath;
    }

    @Override
    public HBox getPane() {
        HBox pane = new HBox();
        pane.setSpacing(10);

        ImageView fileImageView = ImageUtils.loadImageView("folder.png");
        fileImageView.setFitWidth(64);
        fileImageView.setFitHeight(64);

        Text title = new Text(filepath.getFileName().toString());
        title.setFont(new Font(24));

        pane.getChildren().add(fileImageView);
        pane.getChildren().add(title);

        return pane;
    }

    public String getName() {
        return filepath.getFileName().toString();
    }
    public Path getFilePath() {
        return filepath;
    }
}
