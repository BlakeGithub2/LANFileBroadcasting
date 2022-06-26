package main.browse;

import main.Main;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectLoader {
    public static List<Project> loadProjectList() throws IOException {
        List<Project> projects = new ArrayList<>();

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

        return projects;
    }
    private static String loadExternalPathToProject(File internalProjectFile) throws IOException {
        File projectInfo = new File(internalProjectFile.getPath()
                + "/" + Main.PROJECT_INFO_FILE_PATH);

        BufferedReader br = new BufferedReader(new FileReader(projectInfo));
        String filePath = br.readLine();
        br.close();

        return filePath;
    }
    public static File getInternalProjectFile(File projectsFile, String name) {
        return new File(projectsFile.getPath() + "/" + name);
    }
}
