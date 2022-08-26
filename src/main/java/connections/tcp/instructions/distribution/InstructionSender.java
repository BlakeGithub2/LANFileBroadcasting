package connections.tcp.instructions.distribution;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InstructionSender implements PropertyChangeListener {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Map<Long, String> callsAwaitingReturn;
    private Map<Long, Object> returnValues;
    private static long instructionId = 0;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public InstructionSender(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
        this.callsAwaitingReturn = new HashMap<>();
        this.returnValues = new HashMap<>();
    }

    public void send(String instructionStr) throws IOException {
        try {
            System.out.println("Sending: " + instructionId + " " + instructionStr);
            out.writeUTF("" + instructionId + " " + instructionStr);
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        out.flush();

        callsAwaitingReturn.put(instructionId, null);
        instructionId++;
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
        if (evt.getPropertyName().equals("network-response-unregistered")) {
            InstructionResponse response = (InstructionResponse) evt.getNewValue();
            long targetInstruction = response.instructionId();
            Object returnValue = response.returnValue();

            returnValues.put(targetInstruction, returnValue);
            pcs.firePropertyChange("network-response-registered", null, targetInstruction);
        } else if (evt.getPropertyName().equals("network-end-response")) {
            long targetInstruction = (int) evt.getNewValue();

            callsAwaitingReturn.remove(targetInstruction);
            returnValues.remove(targetInstruction);
        }
    }

    public Object getReturnValue(int instructionId) {
        return returnValues.get(instructionId);
    }
}