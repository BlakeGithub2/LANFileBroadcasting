package connections.tcp;

public abstract class TCPInstruction {
    public abstract void execute(TCPClientThread clientThread);
}
