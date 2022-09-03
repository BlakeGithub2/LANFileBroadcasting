package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.browse.download.DownloadWriter;

import java.io.IOException;
import java.util.Map;

public class CreateProjectInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        String projectName = InstructionUtils.parseNameWithSpaces(instruction, 3);
        Map<Long, DownloadWriter> downloadEntryMap = (Map<Long, DownloadWriter>) sender.getTransferredData().get("download-writer");

        downloadEntryMap.put(downloadId, new DownloadWriter());
        boolean success = downloadEntryMap.get(downloadId).createProject(projectName);

        if (!success) {
            sender.sendError(downloadId, "Unable to create project.", "" + downloadId);
            downloadEntryMap.get(downloadId).markFailed();
        }
    }
}
