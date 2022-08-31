package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;

import java.io.IOException;

public class ReturnInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long instructionId = InstructionUtils.parseInstructionId(instruction);
        Object returnValue = InstructionUtils.parseNameWithSpaces(instruction, 2);

        sender.executeResponse(instructionId, returnValue);
    }
}
