package connections.tcp.instructions.distribution;

public interface IErrorableInstruction extends IInstruction {
    void throwError(InstructionSender sender, String errorInstruction);
}
