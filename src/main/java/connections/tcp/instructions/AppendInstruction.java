package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IErrorableInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.browse.download.DownloadEntry;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class AppendInstruction implements IErrorableInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long instructionId = InstructionUtils.parseInstructionId(instruction);
        long downloadId = Long.parseLong(InstructionUtils.parseArgument(instruction, 2));
        String addInformation = InstructionUtils.parseNameWithSpaces(instruction, 3);
        Map<Long, DownloadEntry> downloadEntryMap = (Map<Long, DownloadEntry>) sender.getTransferredData().get("download-tracker");

        boolean success = downloadEntryMap.get(downloadId).appendToCurrentFile(Base64.getDecoder().decode(addInformation));

        if (!success) {
            sender.sendError(instructionId, "Unable to append information to file.");
        }
    }

    @Override
    public void throwError(Map<String, Object> transferredData, String instruction) {

    }
}