package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IRespondableInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import javafx.collections.ObservableList;
import main.browse.Project;
import main.browse.ProjectList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetDownloadsServerInstruction implements IRespondableInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        List<Project> projects = ProjectList.loadProjectList();

        for (Project project : projects) {
            sender.sendReturn(InstructionUtils.parseInstructionId(instruction), project.getName());
        }
    }

    @Override
    public void onResponse(Map<String, Object> transferredData, Object returnValue) {
        ObservableList<String> downloadableProjects = (ObservableList<String>) transferredData.get("downloadable-projects");
        downloadableProjects.add((String) returnValue);
    }
}
