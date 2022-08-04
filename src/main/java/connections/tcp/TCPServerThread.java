package connections.tcp;

import connections.tcp.instructions.InstructionReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TCPServerThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU, https://www.baeldung.com/a-guide-to-java-sockets
    private Socket clientSocket;
    private OutputStream out;
    private OutputStreamWriter outString;
    private InputStream in;
    private Scanner inString;
    private boolean started;

    public TCPServerThread(Socket socket) throws IOException {
        this("TCPServerThread", socket);
    }

    public TCPServerThread(String name, Socket socket) throws IOException {
        super(name);
        clientSocket = socket;
    }

    public void initialize() throws IOException {
        if (!started) {
            out = clientSocket.getOutputStream();
            in = clientSocket.getInputStream();
            outString = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            inString = new Scanner(in).useDelimiter("\n");
            // see: https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
        }
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
                receiveInstruction();
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

    private void receiveInstruction() throws IOException {
        try {
            String instruction = inString.next();
            String[] splitInstruction = instruction.split(" ");

            if (splitInstruction.length == 0) {
                throw new IOException("Invalid instruction.");
            }

            InstructionReceiver.serverReceiveInstruction(out, instruction);
        } catch (NoSuchElementException e) {
            return;
        }
    }
    @Override
    public void interrupt() {
        super.interrupt();

        // Socket close code only works here
    }
}
