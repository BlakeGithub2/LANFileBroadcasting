package example.chatroom;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        System.out.println("Please input username");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        System.out.println("Welcome, " + name + "!");

        int portNumber = 9999;
        try {
            socket = new Socket("127.0.0.1", portNumber);
            Thread.sleep(1000);
            Thread server = new Thread(new ServerThread(socket, name));
            server.start();
        } catch (IOException e) {
            System.err.println("Fatal Connection error!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Fatal Connection error!");
            e.printStackTrace();
        }
    }
}
