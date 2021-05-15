package main.browse;

import javafx.scene.Scene;
import main.Page;

import java.io.File;

public class BrowseFilePage implements Page {
    private Scene scene;

    public void addProject(File selectedDir) {
        Project project = new Project(selectedDir.getAbsoluteFile().toPath());

    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
