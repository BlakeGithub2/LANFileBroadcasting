package examples.chatroom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private static int portNumber = 9999;
    private static ServerSocket serverSocket;
    public static ArrayList<ClientThread> clients;

    public static void main(String[] args) {
        // port numbers lower than 1000 more likely to be already used
        try {
            serverSocket = new ServerSocket(portNumber);
            acceptClients();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + portNumber);
            System.exit(1);
        } finally {

        }
    }

    public static void acceptClients() {
        clients = new ArrayList<>();
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.write("hello newly connected client!");

                ClientThread client = new ClientThread(socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
                System.out.println("Accepted client.");
            } catch (IOException e) {
                System.out.println("Accept failed on: " + portNumber);
            }
        }
    }

    public ArrayList<ClientThread> getClients() {
        return clients;
    }
}
