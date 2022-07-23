package connections.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class TCPMultiServer extends Thread {
    // Thanks to https://www.baeldung.com/a-guide-to-java-sockets
    private ServerSocket serverSocket;
    private List<TCPServerThread> clients;

    public TCPMultiServer() {
        clients = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(4447);
            serverSocket.setSoTimeout(1000);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                TCPServerThread client = new TCPServerThread(serverSocket.accept());
                clients.add(client);
                client.start();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }

        try {
            interruptAllClients();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void interruptAllClients() {
        for (TCPServerThread client : clients) {
            client.interrupt();
        }
    }
}
