package main.browse;

import main.Main;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectList {
    private File file;
    private List<Project> projects;

    public ProjectList(String directoryInBaseFile) throws IOException {
        if (!Main.getBaseFile().exists()) {
            throw new FileNotFoundException("Base file not found.");
        }
        this.file = Main.getBaseFile().getDirectoryAt(directoryInBaseFile);
        this.projects = loadProjects();
    }
    private List<Project> loadProjects() throws IOException {
        List<Project> projects = new ArrayList<>();

        String[] projectNames = file.list();

        for (String name : projectNames) {
            File internalProjectFile = getProjectFile(name);

            // Add project
            if (internalProjectFile.exists()) {
                String externalProjectPathStr = getExternalProjectPath(name);
                Path externalProjectPath = new File(externalProjectPathStr).getCanonicalFile().toPath();
                projects.add(new Project(externalProjectPath));
            } else {
                // TODO: Implement corrupted project
            }
        }

        return projects;
    }

    public boolean add(String projectName) throws IOException {
        if (contains(projectName)) {
            throw new RuntimeException("Cannot add a project that already exists.");
        }
        File newProjectFile = getProjectFile(projectName);
        boolean wasSuccessful = newProjectFile.mkdir();
        projects = loadProjects();

        return wasSuccessful;
    }
    public boolean delete(String projectName) {
        File internalProjectFile = getProjectFile(projectName);
        File projectInfoFile = new File(internalProjectFile + "/" + Main.PROJECT_INFO_FILE_PATH);

        boolean couldDelete;
        couldDelete = projectInfoFile.delete() && internalProjectFile.delete();

        return couldDelete;
    }

    public File getProjectFile(String name) {
        return new File(file + "/" + name);
    }
    public File getProjectInfoFile(String projectName) {
        File projectInfo = new File(getProjectFile(projectName)
                + "/" + Main.PROJECT_INFO_FILE_PATH);
        return projectInfo;
    }
    private String getExternalProjectPath(String projectName) throws IOException {
        File projectInfo = getProjectInfoFile(projectName);

        BufferedReader br = new BufferedReader(new FileReader(projectInfo));
        String filePath = br.readLine();
        br.close();

        return filePath;
    }
    public boolean contains(String projectName) {
        return find(projectName) != null;
    }
    public Project find(String projectName) {
        for (Project project : projects) {
            if (project.getName().equals(projectName)) {
                return project;
            }
        }
        return null;
    }

    public File getFile() {
        return file;
    }
    public List<Project> getProjects() {
        return projects;
    }
}
