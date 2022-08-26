package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.Main;
import main.browse.ProjectList;

import java.io.IOException;

public class CreateProjectServerInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        String projectName = instruction.split(" ")[1];

        if (Main.getBaseFile().exists() && !ProjectList.contains(projectName)) {
            sender.sendReturn(InstructionUtils.parseInstructionId(instruction), "true");  // success
        } else {
            sender.sendReturn(InstructionUtils.parseInstructionId(instruction), "false"); // failure
        }
    }
}
