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
        }

        if (result == null) {
            throw new NullPointerException("No such instruction type found: " + instruction);
        }

        return result;
    }
}
