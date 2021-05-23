package main.browse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import main.Main;
import main.Page;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class BrowseFilePage implements Page {
    private ObservableList<Project> projects = FXCollections.observableArrayList();

    public void addProject(File selectedDir) throws CannotAddRepeatException, FileNotFoundException {
        Project project = new Project(selectedDir.getAbsoluteFile().toPath());

        if (contains(selectedDir)) {
            //throw new CannotAddRepeatException("Project already exists.");
            return;
        } else if (!selectedDir.getAbsoluteFile().exists()) {
            throw new FileNotFoundException("Could not find project file.");
        }

        projects.add(project);
    }

    public void deleteProject(String projectName) {
        Project project = findProject(projectName);

        if (project == null) {
            throw new NullPointerException("No project found with project name: " + projectName);
        }

        deleteProject(project);
    }
    public void deleteProject(Project toDelete) {
        if (toDelete == null) {
            throw new NullPointerException("No project selected for deletion.");
        } else {
            // Delete the project
            projects.remove(toDelete);
        }
    }

    public Project findProject(String projectName) throws NullPointerException {
        if (projectName == null) {
            throw new NullPointerException("Cannot determine if project list contains a project with " +
                    "a null name.");
        }

        for (Project project : projects) {
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

        for (Project project : projects) {
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

        for (Project project : projects) {
            if (project.getFilePath().equals(projectFile.getAbsoluteFile().toPath())) {
                return true;
            }
        }

        return false;
    }

    // TODO: Refactor save/load methods into separate class
    public void save() throws IOException {
        File file = Main.getBaseFile().getDirectoryAt("projects");

        for (Project project : projects) {
            File internalProjectFile = getInternalProjectFile(file, project.getName());
            File projectInfo = new File(internalProjectFile.getPath()
                    + "/" + Main.PROJECT_INFO_FILE_PATH);

            // Create if not already made
            boolean fileCreated = internalProjectFile.mkdir();
            if (fileCreated) {
                projectInfo.createNewFile();
            }

            // Overwrite filepath to project
            BufferedWriter out = new BufferedWriter(new FileWriter(projectInfo));
            out.write(project.getFilePath().toString());
            out.close();
        }

        // Delete duplicate projects
        String[] projectNames = file.list();
        for (String name : projectNames) {
            boolean projectDeleted = true;
            for (Project project : projects) {
                if (project.getName().equals(name)) {
                    projectDeleted = false;
                    break;
                }
            }

            // TODO: If could not delete file, adds project back
            if (projectDeleted) {
                File internalProjectFile = getInternalProjectFile(file, name);
                File projectInfoFile = new File(internalProjectFile + "/" + Main.PROJECT_INFO_FILE_PATH);

                boolean couldDelete;
                couldDelete = projectInfoFile.delete() && internalProjectFile.delete();
                if (!couldDelete) {
                    showCouldNotDeleteMessage();
                }
            }
        }
    }
    private void showCouldNotDeleteMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not delete project.");
        a.showAndWait();
    }
    public void load() throws IOException {
        File file = Main.getBaseFile().getDirectoryAt("projects");
        projects.clear();

        String[] projectNames = file.list();

        for (String name : projectNames) {
            File internalProjectFile = getInternalProjectFile(file, name);

            // Add project
            if (internalProjectFile.exists()) {
                String externalProjectPathStr = loadExternalPathToProject(internalProjectFile);
                Path externalProjectPath = new File(externalProjectPathStr).getCanonicalFile().toPath();
                projects.add(new Project(externalProjectPath));
            } else {
                // TODO: Implement corrupted project
            }
        }
    }
    private String loadExternalPathToProject(File internalProjectFile) throws IOException {
        File projectInfo = new File(internalProjectFile.getPath()
                + "/" + Main.PROJECT_INFO_FILE_PATH);

        BufferedReader br = new BufferedReader(new FileReader(projectInfo));
        String filePath = br.readLine();
        br.close();

        return filePath;
    }
    private File getInternalProjectFile(File projectsFile, String name) {
        return new File(projectsFile.getPath() + "/" + name);
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }
}
