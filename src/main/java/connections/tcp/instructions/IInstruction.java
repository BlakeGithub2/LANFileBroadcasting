package connections.tcp.instructions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface IInstruction {
    void onReceive(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException;
    Object onResponse(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException;
}
