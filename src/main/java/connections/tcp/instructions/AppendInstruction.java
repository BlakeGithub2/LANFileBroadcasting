package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.browse.download.DownloadWriter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class AppendInstruction implements IInstruction {
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        String addInformation = InstructionUtils.parseNameWithSpaces(instruction, 3);
        Map<Long, DownloadWriter> downloadEntryMap = (Map<Long, DownloadWriter>) sender.getTransferredData().get("download-writer");

        boolean success = downloadEntryMap.get(downloadId).appendToCurrentFile(Base64.getDecoder().decode(addInformation));

        if (!success) {
            sender.sendError(downloadId, "Unable to append information to file.", "" + downloadId);
            downloadEntryMap.get(downloadId).markFailed();
        }
    }
}
