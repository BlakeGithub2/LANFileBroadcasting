package connections.tcp.instructions.distribution;

import connections.tcp.instructions.*;

public class InstructionFactory {
    public static IInstruction getInstruction(String instruction) {
        String command = InstructionUtils.parseInstructionType(instruction);
        IInstruction result = null;

        // Instruction factory
        if (command.equals("get-downloads")) {
            result = new GetDownloadsInstruction();
        } else if (command.equals("download")) {
            result = new DownloadProjectInstruction();
        } else if (command.equals("drop-directory")) {
            result = new DropDirectoryInstruction();
        } else if (command.equals("append")) {
            result = new AppendInstruction();
        } else if (command.equals("create-file")) {
            result = new CreateFileInstruction();
        } else if (command.equals("create-directory")) {
            result = new CreateDirectoryInstruction();
        } else if (command.equals("create-project")) {
            result = new CreateProjectInstruction();
        } else if (command.equals("return")) {
            result = new ReturnInstruction();
        } else if (command.equals("error")) {
            result = new ErrorInstruction();
        } else if (command.equals("success")) {
            result = new SuccessInstruction();
        }

        if (result == null) {
            throw new NullPointerException("No such instruction type found: " + instruction);
        }

        return result;
    }
}
