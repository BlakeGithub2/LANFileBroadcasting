package connections.tcp.instructions;

public class InstructionFactory {
    public static IInstruction getInstruction(String instruction) {
        String command = instruction.split(" ")[0];
        IInstruction result = null;

        // Instruction factory
        if (command.equals("get") && instruction.equals("get downloads")) {
            result = new GetDownloadsServerInstruction();
        } else if (command.equals("download")) {
            result = new DownloadProjectServerInstruction();
        } else if (command.equals("create-file")) {
            result = new CreateFileServerInstruction();
        } else if (command.equals("create-project")) {
            result = new CreateProjectServerInstruction();
        } else if (command.equals("append")) {

        }

        if (result == null) {
            throw new NullPointerException("No such instruction type found: " + instruction);
        }

        return result;
    }
}
