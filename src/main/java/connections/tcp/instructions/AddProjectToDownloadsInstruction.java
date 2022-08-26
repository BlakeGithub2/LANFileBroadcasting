package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import javafx.application.Platform;

import java.io.IOException;

public class AddProjectToDownloadsInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        String projectName = instruction.substring(instruction.charAt(' ') + 1);

        Platform.runLater(() -> {
            //downloadableProjects.add(projectName);
        });
    }
}
