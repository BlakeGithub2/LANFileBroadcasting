package connections.tcp;

import connections.tcp.instructions.InstructionReceiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPServerThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU, https://www.baeldung.com/a-guide-to-java-sockets
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public TCPServerThread(Socket socket) throws IOException {
        this("TCPServerThread", socket);
    }

    public TCPServerThread(String name, Socket socket) throws IOException {
        super(name);
        clientSocket = socket;
    }

    public void initialize() throws IOException {
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public void run() {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                InstructionReceiver.readInstructionFromSocket(out, in);
            } catch (IOException e) {
                break;
            }
        }

        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();

        // Socket close code only works here
    }
}
