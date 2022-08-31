package connections.tcp.instructions.distribution;

public interface IOnSuccessInstruction extends IInstruction {
    void onSuccess(InstructionSender sender);
}
