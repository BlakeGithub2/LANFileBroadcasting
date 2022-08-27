package main.browse.viewown;

import main.Main;
import main.browse.BrowseFilePage;
import main.browse.Project;
import main.browse.ProjectList;

import java.io.*;
import java.util.List;

public class BrowseOwnFilePage extends BrowseFilePage {
    public void save() throws IOException {
        if (!Main.getBaseFile().exists()) {
            throw new FileNotFoundException("Base file not found.");
        }

        ProjectList projectList = new ProjectList("projects");

        for (Project project : unsavedProjects) {
            File projectFile = projectList.getProjectFile(project.getName());

            // Create if not already made
            boolean fileCreated = projectFile.mkdir();
            File projectInfoFile = projectList.getProjectInfoFile(project.getName());
            if (fileCreated) {
                projectInfoFile.createNewFile();
            }

            // Overwrite filepath to project
            BufferedWriter out = new BufferedWriter(new FileWriter(projectInfoFile));
            out.write(project.getFilePath().toString());
            out.close();
        }

        // Delete projects that no longer exist
        String[] projectNames = projectList.getFile().list();
        for (String name : projectNames) {
            boolean projectDeleted = true;
            for (Project project : unsavedProjects) {
                if (project.getName().equals(name)) {
                    projectDeleted = false;
                    break;
                }
            }

            // TODO: If could not delete file, adds project back
            if (projectDeleted) {
                boolean couldDelete = projectList.delete(name);
                if (!couldDelete) {
                    throw new IOException("Could not delete project.");
                }
            }
        }
    }
    public void load() throws IOException {
        List<Project> nonObservableProjectsList = new ProjectList("projects").getProjects();
        for (Project project : nonObservableProjectsList) {
            unsavedProjects.add(project);
        }
    }
}
