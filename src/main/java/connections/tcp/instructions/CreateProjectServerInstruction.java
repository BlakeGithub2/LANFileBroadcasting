package connections.tcp.instructions;

import main.Main;
import main.browse.ProjectList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CreateProjectServerInstruction implements IInstruction {
    @Override
    public void onReceive(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        String projectName = instruction.split(" ")[1];

        if (Main.getBaseFile().exists() && !ProjectList.contains(projectName)) {
            out.writeBoolean(true); // success!
        } else {
            out.writeBoolean(false); // failure
        }
        out.flush();
    }

    @Override
    public Object onResponse(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        return in.readBoolean();
    }
}
