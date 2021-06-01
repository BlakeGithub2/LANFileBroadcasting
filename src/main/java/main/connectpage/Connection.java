package main.connectpage;

import java.net.InetAddress;

public class Connection {
    private String name = "";
    private InetAddress ip;

    public Connection(String name, InetAddress ip) {
        this.name = name;
        this.ip = ip;
    }

    public InetAddress getAddress() {
        return ip;
    }
}
