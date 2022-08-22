package connections.tcp.instructions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class InstructionSender {
    public static Object sendInstruction(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        try {
            out.writeUTF(instruction);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();

        Object result = null;
        try {
            result = InstructionReceiver.serverReturnInstruction(out, in, instruction);
        }  catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        return result;
    }
}
