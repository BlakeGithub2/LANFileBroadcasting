package main.browse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import main.Main;
import main.Page;

import java.io.File;

public class BrowseFilePage implements Page {
    private Scene scene;
    private ObservableList<Project> projects = FXCollections.observableArrayList();

    public BrowseFilePage() {
        scene = Main.getSceneController().getScene("browse");
    }

    public void addProject(File selectedDir) {
        Project project = new Project(selectedDir.getAbsoluteFile().toPath());
        projects.add(project);
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
