package connections.tcp.instructions.distribution;

import java.io.IOException;

public interface IInstruction {
    void onReceive(InstructionSender sender, String instruction) throws IOException;
}
