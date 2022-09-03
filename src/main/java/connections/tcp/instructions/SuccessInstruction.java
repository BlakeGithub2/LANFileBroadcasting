package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;

import java.io.IOException;

public class SuccessInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        System.out.println("received success");
        sender.terminateCall(InstructionUtils.parseInstructionId(instruction), true);
    }
}
