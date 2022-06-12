package connections.tcp;

import main.browse.download.DownloadPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class TCPClientThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU
    private Socket socket;
    private PrintWriter out;
    private DownloadPage page;
    private Queue<TCPInstruction> instructions;

    public TCPClientThread(InetAddress host, DownloadPage page) throws IOException {
        this("TCPClientThread", host, page);
    }

    public TCPClientThread(String name, InetAddress host, DownloadPage page) throws IOException {
        super(name);
        socket = new Socket(host, 4447);
        out = new PrintWriter(socket.getOutputStream(), true);
        instructions = new LinkedList<>();
        this.page = page;
    }

    /*
    TODO: Find a better way to separate protocol from clients
     */
    @Override
    public void run() {
        while (isAlive()) {
            while (instructions.size() > 0) {
                instructions.poll();
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
        System.out.println("Added instruction");
    }

    public PrintWriter getOutputWriter() {
        return out;
    }

    public DownloadPage getPage() {
        return page;
    }
}
