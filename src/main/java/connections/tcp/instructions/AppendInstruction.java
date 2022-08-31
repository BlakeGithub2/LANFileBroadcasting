package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IErrorableInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.browse.download.DownloadWriter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class AppendInstruction implements IErrorableInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long instructionId = InstructionUtils.parseInstructionId(instruction);
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        String addInformation = InstructionUtils.parseNameWithSpaces(instruction, 3);
        Map<Long, DownloadWriter> downloadEntryMap = (Map<Long, DownloadWriter>) sender.getTransferredData().get("download-writer");

        boolean success = downloadEntryMap.get(downloadId).appendToCurrentFile(Base64.getDecoder().decode(addInformation));

        if (!success) {
            sender.sendError(instructionId, "Unable to append information to file.", "" + downloadId);
            downloadEntryMap.get(downloadId).markFailed();
        }
    }

    @Override
    public void throwError(InstructionSender sender, String errorInstruction) {
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(errorInstruction, 2));
        String error = InstructionUtils.parseNameWithSpaces(errorInstruction, 3);

        DownloadProjectInstruction.recordError(error, downloadId);
    }
}
