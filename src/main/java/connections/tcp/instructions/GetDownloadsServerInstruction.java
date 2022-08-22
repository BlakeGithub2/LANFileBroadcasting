package connections.tcp.instructions;

import main.browse.Project;
import main.browse.ProjectList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GetDownloadsServerInstruction implements IInstruction {
    @Override
    public void onReceive(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        List<Project> projects = ProjectList.loadProjectList();

        List<String> projectNames = new ArrayList<>();
        for (Project project : projects) {
            projectNames.add(project.getName());
        }

        out.writeObject(projectNames);
        out.flush();
    }

    @Override
    public Object onResponse(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        try {
            Object result = in.readObject();
            return result;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnknownError("Could not find returned object from instruction: " + instruction);
        }
    }
}
