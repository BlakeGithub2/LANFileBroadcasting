package connections.tcp.instructions.distribution;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InstructionSender {
    private ObjectOutputStream out;
    private Map<Long, String> callsAwaitingReturn;
    private Map<String, Object> transferredData;
    private static long instructionId = 0;

    public InstructionSender(ObjectOutputStream out) {
        this.out = out;
        this.callsAwaitingReturn = new HashMap<>();
        this.transferredData = new HashMap<>();
    }

    public long send(String instructionStr) throws IOException {
        String finishedInstructionStr = instructionId + " " + instructionStr;
        IInstruction instruction = InstructionFactory.getInstruction(finishedInstructionStr);

        if (instruction instanceof IOnSendableInstruction) {
            ((IOnSendableInstruction) instruction).onSend(transferredData, instructionStr);
        }

        try {
            out.writeUTF("" + finishedInstructionStr);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();

        if (instruction instanceof IRespondableInstruction || instruction instanceof IErrorableInstruction) {
            callsAwaitingReturn.put(instructionId, finishedInstructionStr);
            return instructionId++;
        } else {
            return instructionId;
        }
    }
    public void sendSuccess(long receivedInstructionId) throws IOException {
        try {
            out.writeUTF("" + receivedInstructionId + " success");
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();
    }
    public void sendError(long receivedInstructionId, String errorMessage) throws IOException {
        try {
            out.writeUTF("" + receivedInstructionId + " error " + errorMessage);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();
    }
    public void sendReturn(long receivedInstructionId, String value) throws IOException {
        try {
            out.writeUTF("" + receivedInstructionId + " return " + value);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();
    }

    public void errorCall(long instructionId) {
        if (!callsAwaitingReturn.containsKey(instructionId)) {
            throw new RuntimeException("Response ID " + instructionId + " was not found when searching for open calls.");
        }

        String instructionStr = callsAwaitingReturn.get(instructionId);
        IErrorableInstruction instruction = (IErrorableInstruction) InstructionFactory.getInstruction(instructionStr);
        instruction.throwError(transferredData, instructionStr);

        terminateCall(instructionId);
    }

    public void terminateCall(long instructionId) {
        if (!callsAwaitingReturn.containsKey(instructionId)) {
            throw new RuntimeException("Response ID " + instructionId + " was not found when searching for open calls.");
        }

        callsAwaitingReturn.remove(instructionId);
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

    public Map<String, Object> getTransferredData() {
        return transferredData;
    }
}