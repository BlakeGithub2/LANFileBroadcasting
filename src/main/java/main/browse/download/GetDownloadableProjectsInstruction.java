package main.browse.download;

import connections.tcp.TCPClientThread;
import connections.tcp.TCPInstruction;

import java.util.List;

public class GetDownloadableProjectsInstruction extends TCPInstruction {
    private List addTo;

    public GetDownloadableProjectsInstruction(List addTo) {
        this.addTo = addTo;
    }

    @Override
    public void execute(TCPClientThread tcpClientThread) {
        tcpClientThread.getOutputWriter().write("req dlds");
    }
}
