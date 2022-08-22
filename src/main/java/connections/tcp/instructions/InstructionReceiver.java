package connections.tcp.instructions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;

public class InstructionReceiver {
    public static boolean readInstructionFromSocket(ObjectOutputStream out, ObjectInputStream in) throws IOException {
        try {
            String instruction = in.readUTF();
            String[] splitInstruction = instruction.split(" ");

            if (splitInstruction.length == 0) {
                throw new IOException("Invalid instruction.");
            }
            if (splitInstruction[0].equalsIgnoreCase("done")) {
                return false;
            }

            InstructionReceiver.serverReceiveInstruction(out, in, instruction);
        } catch (NoSuchElementException e) {
        }

        return true;
    }
    public static void serverReceiveInstruction(ObjectOutputStream out, ObjectInputStream in, String instructionString) throws IOException {
        IInstruction instruction = InstructionFactory.getInstruction(instructionString);
        instruction.onReceive(out, in, instructionString);
    }
    public static Object serverReturnInstruction(ObjectOutputStream out, ObjectInputStream in, String instructionString) throws IOException {
        IInstruction instruction = InstructionFactory.getInstruction(instructionString);
        return instruction.onResponse(out, in, instructionString);
    }
}
