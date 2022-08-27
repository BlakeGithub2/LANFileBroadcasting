package connections.tcp.instructions.distribution;

import java.util.Map;

public interface IErrorableInstruction extends IInstruction {
    void throwError(Map<String, Object> transferredData, String instruction);
}
