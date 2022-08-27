package main.browse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Page;

import java.io.File;
import java.io.FileNotFoundException;

public class BrowseFilePage implements Page {
    protected ObservableList<Project> unsavedProjects = FXCollections.observableArrayList();

    public void addProject(File selectedDir) throws CannotAddRepeatException, FileNotFoundException {
        Project project = new Project(selectedDir.getAbsoluteFile().toPath());

        if (contains(selectedDir)) {
            //throw new CannotAddRepeatException("Project already exists.");
            return;
        } else if (!selectedDir.getAbsoluteFile().exists()) {
            throw new FileNotFoundException("Could not find project file.");
        }

        unsavedProjects.add(project);
    }

    public void deleteProject(String projectName) throws FileNotFoundException {
        Project project = findProject(projectName);

        if (project == null) {
            throw new FileNotFoundException("No project found with project name: " + projectName);
        }

        deleteProject(project);
    }
    public void deleteProject(Project toDelete) {
        if (toDelete == null) {
            throw new NullPointerException("No project selected for deletion.");
        } else {
            // Delete the project
            unsavedProjects.remove(toDelete);
        }
    }

    public Project findProject(String projectName) throws NullPointerException {
        if (projectName == null) {
            throw new NullPointerException("Cannot determine if project list contains a project with " +
                    "a null name.");
        }

        for (Project project : unsavedProjects) {
            if (project.getName().equals(projectName)) {
                return project;
            }
        }

        return null;
    }
    public boolean contains(String projectName) throws NullPointerException {
        if (projectName == null) {
            throw new NullPointerException("Cannot determine if project list contains a project with " +
                    "a null name.");
        }

        for (Project project : unsavedProjects) {
            if (projectName.equals(project.getName())) {
                return true;
            }
        }

        return false;
    }
    public boolean contains(File projectFile) throws NullPointerException {
        if (projectFile == null) {
            throw new NullPointerException("Cannot determine if project list contains a project with " +
                    "a null name.");
        }

        for (Project project : unsavedProjects) {
            if (project.getFilePath().equals(projectFile.getAbsoluteFile().toPath())) {
                return true;
            }
        }

        return false;
    }

    public ObservableList<Project> getUnsavedProjects() {
        return unsavedProjects;
    }
}
