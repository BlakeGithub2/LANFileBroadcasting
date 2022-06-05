package examples.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends ChatServer implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!socket.isClosed()) {
                System.out.println("Socket is not closed.");
                String input = in.readLine();
                System.out.println("Input: " + input);
                if (input != null) {
                    System.out.println("Number of clients: " + getClients().size());
                    for (ClientThread client : getClients()) {
                        System.out.println("Sent data to client.");
                        System.out.println("Socket: " + socket);
                        client.getWriter().println(input);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in ClientThread.java.");
            e.printStackTrace();
        }
    }

    public PrintWriter getWriter() {
        return out;
    }
}
