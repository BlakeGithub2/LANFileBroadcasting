package broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BroadcastClientThread extends Thread {
    private MulticastSocket socket = null;
    private InetAddress address = null;

    public BroadcastClientThread() throws IOException {
        this("BroadcastClientThread");
    }

    public BroadcastClientThread(String name) throws IOException {
        super(name);
        socket = new MulticastSocket(4446);
        address = InetAddress.getByName("230.0.0.255");
        socket.joinGroup(address);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Searching for broadcasted messages...");
            try {
                DatagramPacket packet;

                byte[] buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received Message from Broadcast: " + received);

                socket.leaveGroup(address);
            } catch (IOException e) {
                System.out.println("Could not receive broadcasted messages.");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Could not delay searching for broadcasted messages.");
                e.printStackTrace();
            }
        }
        //socket.close();
    }
}
