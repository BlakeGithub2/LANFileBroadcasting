package main.browse.viewown;

import main.Main;
import main.browse.BrowseFilePage;
import main.browse.Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class BrowseOwnFilePage extends BrowseFilePage {
    public void save() throws IOException {
        if (!Main.getBaseFile().exists()) {
            throw new FileNotFoundException("Base file not found.");
        }

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
                    throw new IOException("Could not delete project.");
                }
            }
        }
    }
    public void load() throws IOException {
        if (!Main.getBaseFile().exists()) {
            throw new FileNotFoundException("Base file not found.");
        }

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
}
