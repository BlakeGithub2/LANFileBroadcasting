package connections.tcp.instructions.distribution;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class InstructionReceiver {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public InstructionReceiver(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
    }

    public boolean executeInstructionFromSocket(InstructionSender sender) throws IOException {
        String instruction = in.readUTF();
        System.out.println("Received: " + instruction);

        if (InstructionUtils.parseInstructionType(instruction).equalsIgnoreCase("done")) {
            return false;
        }

        executeInstruction(sender, instruction);

        return true;
    }
    private void executeInstruction(InstructionSender sender, String instructionString) throws IOException {
        IInstruction instruction = InstructionFactory.getInstruction(instructionString);
        instruction.onReceive(sender, instructionString);
    }
}
