package main.browse;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import main.Main;
import main.Page;

import java.io.File;

public class BrowseFilePage implements Page {
    private Scene scene;
    private ProjectList projects;
    //private ObservableList<Project> projects = FXCollections.observableArrayList();

    public BrowseFilePage() {
        scene = Main.getSceneController().getScene("browse");
        //projects = new ProjectList(filePane);
    }

    public void createProjectsList(VBox pane) {
        projects = new ProjectList(pane);
    }

    public void addProject(File selectedDir) {
        Project project = new Project(selectedDir.getAbsoluteFile().toPath());
        projects.addProject(project);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
