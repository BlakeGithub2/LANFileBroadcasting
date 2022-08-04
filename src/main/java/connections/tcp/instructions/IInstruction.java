package connections.tcp.instructions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IInstruction {
    void onReceive(OutputStream out, String instruction) throws IOException;
    Object onReturn(InputStream in, String instruction) throws IOException;
}
