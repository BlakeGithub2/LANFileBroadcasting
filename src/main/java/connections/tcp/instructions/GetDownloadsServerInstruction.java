package connections.tcp.instructions;

import main.browse.Project;
import main.browse.ProjectLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GetDownloadsServerInstruction implements IInstruction {
    @Override
    public void onReceive(OutputStream out, String instruction) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        List<Project> projects = ProjectLoader.loadProjectList();

        List<String> projectNames = new ArrayList<>();
        for (Project project : projects) {
            projectNames.add(project.getName());
        }

        oos.writeObject(projectNames);
        oos.flush();
    }

    @Override
    public Object onReturn(InputStream in, String instruction) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(in);
        try {
            Object result = ois.readObject();
            return result;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnknownError("Could not find returned object from instruction: " + instruction);
        }
    }
}
