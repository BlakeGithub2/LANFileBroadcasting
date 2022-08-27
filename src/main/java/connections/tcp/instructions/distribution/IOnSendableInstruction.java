package connections.tcp.instructions.distribution;

import java.util.Map;

public interface IOnSendableInstruction extends IInstruction {
    void onSend(Map<String, Object> transferredData, String instruction);
}
