package connections.tcp.instructions.distribution;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InstructionSender {
    private ObjectOutputStream out;
    private Map<Long, String> callsAwaitingReturn;
    private Map<String, Object> transferredData;
    private static long nextAvailableInstructionId = 0;

    public InstructionSender(ObjectOutputStream out) {
        this.out = out;
        this.callsAwaitingReturn = new HashMap<>();
        this.transferredData = new HashMap<>();
    }

    public long send(String instructionStr) throws IOException {
        long instructionId = nextAvailableInstructionId++;

        String finishedInstructionStr = "" + instructionId + " " + instructionStr;
        IInstruction instruction = InstructionFactory.getInstruction(finishedInstructionStr);

        if (instruction instanceof IOnSendableInstruction) {
            ((IOnSendableInstruction) instruction).onSend(transferredData, instructionStr);
        }

        sendMessageThroughStream(finishedInstructionStr);

        if (instruction instanceof IRespondableInstruction || instruction instanceof IErrorableInstruction || instruction instanceof IOnSuccessInstruction) {
            callsAwaitingReturn.put(instructionId, finishedInstructionStr);
        }

        return instructionId;
    }
    public void sendSuccess(long receivedInstructionId) throws IOException {
        sendMessageThroughStream("" + receivedInstructionId + " success");
    }
    public void sendError(long receivedInstructionId, String errorMessage, String... arguments) throws IOException {
        StringBuilder argumentsString = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (i > 0) {
                argumentsString.append(' ');
            }
            argumentsString.append(arguments[i]);
        }

        String instruction = "" + receivedInstructionId + " error " + argumentsString + " " + errorMessage;
        if (argumentsString.isEmpty()) {
            instruction = receivedInstructionId + " error " + errorMessage;
        }

        sendMessageThroughStream(instruction);
    }
    public void sendReturn(long receivedInstructionId, String value) throws IOException {
        sendMessageThroughStream("" + receivedInstructionId + " return " + value);
    }
    private void sendMessageThroughStream(String message) throws IOException {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();
    }

    public void errorCall(long erroredCallInstructionId, String errorInstruction) {
        if (!callsAwaitingReturn.containsKey(erroredCallInstructionId)) {
            throw new RuntimeException("Response ID " + erroredCallInstructionId + " was not found when searching for open calls.");
        }

        String erroredInstructionStr = callsAwaitingReturn.get(erroredCallInstructionId);
        IErrorableInstruction erroredInstruction = (IErrorableInstruction) InstructionFactory.getInstruction(erroredInstructionStr);
        erroredInstruction.throwError(this, errorInstruction);

        terminateCall(erroredCallInstructionId, false);
    }

    public void terminateCall(long instructionId, boolean successful) {
        if (!callsAwaitingReturn.containsKey(instructionId)) {
            throw new RuntimeException("Response ID " + instructionId + " was not found when searching for open calls.");
        }

        if (successful) {
            String instructionStr = callsAwaitingReturn.get(instructionId);
            IOnSuccessInstruction instruction = (IOnSuccessInstruction) InstructionFactory.getInstruction(instructionStr);
            instruction.onSuccess(this);
        }

        callsAwaitingReturn.remove(instructionId);
        System.out.println("" + instructionId + " cleared. " + successful);
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