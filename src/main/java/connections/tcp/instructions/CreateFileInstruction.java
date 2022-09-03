package connections.tcp.instructions;

import connections.tcp.instructions.distribution.*;
import main.browse.download.DownloadWriter;

import java.io.IOException;
import java.util.Map;

public class CreateFileInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        String fileName = InstructionUtils.parseNameWithSpaces(instruction, 3);
        Map<Long, DownloadWriter> downloadEntryMap = (Map<Long, DownloadWriter>) sender.getTransferredData().get("download-writer");

        boolean success = downloadEntryMap.get(downloadId).createFile(fileName);

        if (!success) {
            sender.sendError(downloadId, "Unable to create file " + fileName + ".", "" + downloadId);
            downloadEntryMap.get(downloadId).markFailed();
        }
    }
}
