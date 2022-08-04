package connections.tcp.instructions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InstructionReceiver {
    public static void serverReceiveInstruction(OutputStream out, String instructionString) throws IOException {
        IInstruction instruction = InstructionFactory.getInstruction(instructionString);
        instruction.onReceive(out, instructionString);
    }
    public static Object serverReturnInstruction(InputStream in, String instructionString) throws IOException {
        IInstruction instruction = InstructionFactory.getInstruction(instructionString);
        return instruction.onReturn(in, instructionString);
    }
}
