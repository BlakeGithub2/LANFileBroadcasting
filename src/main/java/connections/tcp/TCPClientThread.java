package connections.tcp;

import connections.tcp.instructions.InstructionReceiver;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPClientThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU
    private Socket socket;
    private OutputStreamWriter outString;
    private Scanner inString;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public TCPClientThread(InetAddress host) throws IOException {
        this("TCPClientThread", host);
    }

    public TCPClientThread(String name, InetAddress host) throws IOException {
        super(name);
        socket = new Socket(host, 4447);
        outString = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
        inString = new Scanner(socket.getInputStream()).useDelimiter("\\A");
    }

    public void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("connected", l);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        try {
            socket.getOutputStream().close();
        } catch (IOException e) {
            notifyDisconnected();
        }
    }

    public Object sendInstruction(String instruction) throws IOException {
        try {
            outString.write(instruction + "\n");
        } catch (IOException e) {
            throw new IOException("Connection closed by server.");
        }
        outString.flush();

        Object result = null;
        try {
            result = InstructionReceiver.serverReturnInstruction(socket.getInputStream(), instruction);
        }  catch (IOException e) {
            notifyDisconnected();
            throw new IOException(e.getMessage());
        }

        return result;
    }

    public void notifyDisconnected() {
        pcs.firePropertyChange("connected", true, false);
    }
}
