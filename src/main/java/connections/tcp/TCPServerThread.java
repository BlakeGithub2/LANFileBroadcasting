package connections.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServerThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU, https://www.baeldung.com/a-guide-to-java-sockets
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean started;

    public TCPServerThread() throws IOException {
        this("TCPServerThread");
    }

    public TCPServerThread(String name) throws IOException {
        super(name);
        serverSocket = new ServerSocket(4447);
        started = false;
    }

    public void initialize() throws IOException {
        if (!started) {
            clientSocket = serverSocket.accept();
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        }
    }

    @Override
    public void run() {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (isAlive()) {
            try {
                receiveInstruction();
            } catch (IOException e) {
            }
        }
    }

    private void receiveInstruction() throws IOException {
        String instruction = in.readUTF();
        if (instruction.equals("get downloads")) {
            List<String> projects = new ArrayList<>();
            projects.add("test");
            projects.add("test2");

            out.writeObject(projects);
            out.flush();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();

        // Socket close code only works here
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
