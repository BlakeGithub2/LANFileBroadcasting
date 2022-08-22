package connections.tcp.instructions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CreateFileServerInstruction implements IInstruction {
    @Override
    public void onReceive(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        // TODO: Create file
        System.out.println("Received create instruction: " + instruction);

    }

    @Override
    public Object onResponse(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        // TODO: Return if file creation was successful
        return true;
    }
}
