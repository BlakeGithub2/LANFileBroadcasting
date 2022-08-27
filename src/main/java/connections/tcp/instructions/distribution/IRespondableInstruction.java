package connections.tcp.instructions.distribution;

import java.util.Map;

public interface IRespondableInstruction extends IInstruction {
    void onResponse(Map<String, Object> transferredData, Object returnValue);
}
