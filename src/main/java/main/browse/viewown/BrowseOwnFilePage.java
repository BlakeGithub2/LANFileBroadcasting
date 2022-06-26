package main.browse.viewown;

import main.Main;
import main.browse.BrowseFilePage;
import main.browse.Project;
import main.browse.ProjectLoader;

import java.io.*;
import java.util.List;

public class BrowseOwnFilePage extends BrowseFilePage {
    public void save() throws IOException {
        if (!Main.getBaseFile().exists()) {
            throw new FileNotFoundException("Base file not found.");
        }

        File file = Main.getBaseFile().getDirectoryAt("projects");

        for (Project project : projects) {
            File internalProjectFile = ProjectLoader.getInternalProjectFile(file, project.getName());
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
                File internalProjectFile = ProjectLoader.getInternalProjectFile(file, name);
                File projectInfoFile = new File(internalProjectFile + "/" + Main.PROJECT_INFO_FILE_PATH);

                boolean couldDelete;
                couldDelete = projectInfoFile.delete() && internalProjectFile.delete();
                if (!couldDelete) {
                    throw new IOException("Could not delete project.");
                }
            }
        }
    }
    public void load() throws IOException {
        List<Project> nonObservableProjectsList = ProjectLoader.loadProjectList();
        for (Project project : nonObservableProjectsList) {
            projects.add(project);
        }
    }
}
