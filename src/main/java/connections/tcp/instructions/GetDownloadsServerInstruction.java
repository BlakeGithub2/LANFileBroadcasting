package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.browse.Project;
import main.browse.ProjectList;

import java.io.IOException;
import java.util.List;

public class GetDownloadsServerInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        List<Project> projects = ProjectList.loadProjectList();

        for (Project project : projects) {
            sender.sendReturn(InstructionUtils.parseInstructionId(instruction), project.getName());
        }
    }
}
