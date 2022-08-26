package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionResponse;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;

import java.beans.PropertyChangeSupport;
import java.io.IOException;

public class ReturnInstruction implements IInstruction {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private long lastChange = -1;

    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        long instructionId = InstructionUtils.parseInstructionId(instruction);
        String instructionResponse = InstructionUtils.parseNameWithSpaces(instruction, 2);
        pcs.firePropertyChange("network-response-unregistered", lastChange,
                new InstructionResponse(instructionId, instructionResponse));
        lastChange = instructionId;
    }
}
