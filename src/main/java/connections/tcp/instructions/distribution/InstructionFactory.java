package connections.tcp.instructions.distribution;

import connections.tcp.instructions.*;

public class InstructionFactory {
    public static IInstruction getInstruction(String instruction) {
        String command = InstructionUtils.parseInstructionType(instruction);
        IInstruction result = null;

        // Instruction factory
        if (command.equals("get-downloads")) {
            result = new GetDownloadsServerInstruction();
        } else if (command.equals("download")) {
            result = new DownloadProjectServerInstruction();
        } else if (command.equals("create-file")) {
            result = new CreateFileServerInstruction();
        } else if (command.equals("create-project")) {
            result = new CreateProjectServerInstruction();
        } else if (command.equals("append")) {
        } else if (command.equals("return")) {
            result = new ReturnInstruction();
        } else if (command.equals("error")) {
            result = new ErrorInstruction();
        }

        if (result == null) {
            throw new NullPointerException("No such instruction type found: " + instruction);
        }

        return result;
    }
}
