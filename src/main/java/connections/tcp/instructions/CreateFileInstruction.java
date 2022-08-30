package connections.tcp.instructions;

import connections.tcp.instructions.distribution.*;
import main.browse.download.DownloadEntry;

import java.io.IOException;
import java.util.Map;

public class CreateFileInstruction implements IErrorableInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long instructionId = InstructionUtils.parseInstructionId(instruction);
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        String fileName = InstructionUtils.parseNameWithSpaces(instruction, 3);
        Map<Long, DownloadEntry> downloadEntryMap = (Map<Long, DownloadEntry>) sender.getTransferredData().get("download-tracker");

        boolean success = downloadEntryMap.get(downloadId).createFile(fileName);

        if (!success) {
            sender.sendError(instructionId, "Unable to create file " + fileName + ".");
        }
    }

    @Override
    public void throwError(Map<String, Object> transferredData, String instruction) {
        Map<Long, DownloadEntry> downloadEntryMap = (Map<Long, DownloadEntry>) transferredData.get("download-tracker");
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        DownloadEntry download = downloadEntryMap.get(downloadId);

        if (!download.hasFailed()) {
            download.markFailed();
        }
    }
}
