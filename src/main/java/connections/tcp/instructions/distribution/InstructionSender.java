package connections.tcp.instructions.distribution;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InstructionSender implements PropertyChangeListener {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Map<Long, String> callsAwaitingReturn;
    private Map<String, Object> transferredData;
    private static long instructionId = 0;

    public InstructionSender(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
        this.callsAwaitingReturn = new HashMap<>();
        this.transferredData = new HashMap<>();
    }

    public long send(String instructionStr) throws IOException {
        String finishedInstructionStr = instructionId + " " + instructionStr;

        try {
            System.out.println("Sending: " + finishedInstructionStr);
            out.writeUTF("" + finishedInstructionStr);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();

        callsAwaitingReturn.put(instructionId, finishedInstructionStr);
        return instructionId++;
    }
    public void sendReturn(long receivedInstructionId, String value) throws IOException {
        try {
            System.out.println("Sending: " + receivedInstructionId + " return " + value);
            out.writeUTF("" + receivedInstructionId + " return " + value);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("network-end-response")) {
            long targetInstruction = (int) evt.getNewValue();

            callsAwaitingReturn.remove(targetInstruction);
        }
    }

    public void executeResponse(long responseId, Object returnValue) {
        if (!callsAwaitingReturn.containsKey(responseId)) {
            throw new RuntimeException("Response ID " + responseId + " was not found when searching for open calls.");
        }

        IRespondableInstruction instruction =
                (IRespondableInstruction) InstructionFactory.getInstruction(callsAwaitingReturn.get(responseId));
        instruction.onResponse(transferredData, returnValue);
    }

    public void addNetworkData(String name, Object obj) {
        transferredData.put(name, obj);
    }
}