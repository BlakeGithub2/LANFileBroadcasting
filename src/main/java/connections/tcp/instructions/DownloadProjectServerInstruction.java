package connections.tcp.instructions;

import main.browse.Project;
import main.browse.ProjectLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class DownloadProjectServerInstruction implements IInstruction {
    @Override
    public void onReceive(OutputStream out, String instruction) throws IOException {
        String projectName = instruction.substring(instruction.indexOf(' ') + 1);
        OutputStreamWriter outString = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        List<Project> projects = ProjectLoader.loadProjectList();

        if (!projectListContains(projects, projectName)) {
            outString.write("Project not found.\n");
            outString.flush();
            return;
        }

        outString.write("Project found.\n");
        outString.flush();
    }

    @Override
    public Object onReturn(InputStream in, String instruction) throws IOException {
        Scanner inString = new Scanner(in).useDelimiter("\n");
        return inString.next();
    }

    private boolean projectListContains(List<Project> projectList, String projectName) {
        for (Project project : projectList) {
            if (project.getName().equals(projectName)) {
                return true;
            }
        }
        return false;
    }
}
