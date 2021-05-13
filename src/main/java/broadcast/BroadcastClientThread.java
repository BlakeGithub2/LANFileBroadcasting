/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package broadcast;

import main.connectpage.ConnectPage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BroadcastClientThread extends Thread {
    private MulticastSocket socket;
    private InetAddress address;
    private ConnectPage page;

    private static final int FIVE_SECONDS = 5000;

    public BroadcastClientThread(ConnectPage page) throws IOException {
        this("BroadcastClientThread", page);
    }

    public BroadcastClientThread(String name, ConnectPage page) throws IOException {
        super(name);
        socket = new MulticastSocket(4446);
        socket.setSoTimeout(FIVE_SECONDS);
        address = InetAddress.getByName("230.0.0.255");
        socket.joinGroup(address);
        this.page = page;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet;

                byte[] buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length);

                // Packet was received
                socket.receive(packet);

                // Find received message
                String received = new String(packet.getData(), 0, packet.getLength());
                page.getConnectionList().addConnection(received, packet.getAddress());
            } catch (IOException e) {
                System.out.println("Could not receive broadcasted messages.");
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Could not delay searching for broadcasted messages.");
                e.printStackTrace();
            }
        }

        try {
            socket.leaveGroup(address);
        } catch (IOException e) {
            System.out.println("Socket could not leave group.");
            e.printStackTrace();
        }
        socket.close();
    }
}
