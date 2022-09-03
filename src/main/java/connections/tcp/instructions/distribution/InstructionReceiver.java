package connections.tcp.instructions.distribution;

import java.io.BufferedReader;
import java.io.IOException;

public class InstructionReceiver {
    private BufferedReader in;

    public InstructionReceiver(BufferedReader in) {
        this.in = in;
    }

    public boolean executeInstructionFromSocket(InstructionSender sender) throws IOException {
        String instruction = in.readLine();

        if (instruction != null) {
            executeInstruction(sender, instruction);
        } else {
            System.out.println("no instruction");
        }

        return true;
    }
    private void executeInstruction(InstructionSender sender, String instructionString) throws IOException {
        IInstruction instruction = InstructionFactory.getInstruction(instructionString);
        instruction.onReceive(sender, instructionString);
    }
}
