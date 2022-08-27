package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IErrorableInstruction;
import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.browse.download.DownloadEntry;

import java.io.IOException;
import java.util.Map;

public class DropDirectoryInstruction implements IInstruction, IErrorableInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long instructionId = InstructionUtils.parseInstructionId(instruction);
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        Map<Long, DownloadEntry> downloadEntryMap = (Map<Long, DownloadEntry>) sender.getTransferredData().get("download-tracker");

        boolean success = downloadEntryMap.get(downloadId).dropDirectory();

        if (!success) {
            sender.sendError(instructionId, "Unable to drop down a directory.");
        }
    }

    @Override
    public void throwError(Map<String, Object> transferredData, String instruction) {

    }
}
