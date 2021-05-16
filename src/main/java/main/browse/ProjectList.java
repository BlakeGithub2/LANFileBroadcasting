package main.browse;

import javafx.scene.layout.Pane;
import main.GUIPaneList;

public class ProjectList extends GUIPaneList {
    public ProjectList(Pane pane) {
        super(pane);
    }

    public void addProject(Project p) {
        add(p);
        System.out.println("Project added.");
    }
}
