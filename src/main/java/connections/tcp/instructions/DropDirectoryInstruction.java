package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IErrorableInstruction;
import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.browse.download.DownloadWriter;

import java.io.IOException;
import java.util.Map;

public class DropDirectoryInstruction implements IInstruction, IErrorableInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long instructionId = InstructionUtils.parseInstructionId(instruction);
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        Map<Long, DownloadWriter> downloadEntryMap = (Map<Long, DownloadWriter>) sender.getTransferredData().get("download-writer");

        boolean success = downloadEntryMap.get(downloadId).dropDirectory();

        if (!success) {
            sender.sendError(instructionId, "Unable to drop down a directory.", "" + downloadId);
        }
    }

    @Override
    public void throwError(InstructionSender sender, String errorInstruction) {
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(errorInstruction, 2));
        String error = InstructionUtils.parseNameWithSpaces(errorInstruction, 3);

        DownloadProjectInstruction.recordError(error, downloadId);
    }
}
