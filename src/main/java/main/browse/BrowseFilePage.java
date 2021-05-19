package main.browse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import main.Main;
import main.Page;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

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
    }
    public void load() throws IOException {
        File file = Main.getBaseFile().getDirectoryAt("projects");

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

        return filePath;
    }
    private File getInternalProjectFile(File projectsFile, String name) {
        return new File(projectsFile.getPath() + "/" + name);
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
