package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;

import java.io.IOException;

public class CreateFileServerInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        // TODO: Create file
        System.out.println("Received create instruction: " + instruction);

    }
}
