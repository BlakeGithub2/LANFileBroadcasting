package connections.tcp;

import connections.ConnectionThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class TCPClientThread extends ConnectionThread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU
    private Socket socket;
    private PrintWriter out;
    private Queue<TCPInstruction> instructions;

    public TCPClientThread(InetAddress host) throws IOException {
        this("TCPClientThread", host);
    }

    public TCPClientThread(String name, InetAddress host) throws IOException {
        super(name);
        socket = new Socket(host, 4447);
        out = new PrintWriter(socket.getOutputStream(), true);
        instructions = new LinkedList<>();
    }

    @Override
    public void run() {
        while (!shouldStop) {
            while (instructions.size() > 0) {
                instructions.poll().execute(this);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addInstruction(TCPInstruction instruction) {
        instructions.add(instruction);
    }

    public PrintWriter getOutputWriter() {
        return out;
    }
}
