package main.browse.download;

import connections.tcp.TCPClientThread;
import main.Main;
import main.browse.BrowseFilePage;
import main.browse.Project;

import java.util.List;

public class DownloadPage extends BrowseFilePage {
    private TCPClientThread client;

    public List<Project> getDownloadableProjects() {
        return projects;
    }

    public void pullTransferredInformation() {
        client = (TCPClientThread) Main.getSceneController().getTransferredData("tcpClient");
        System.out.println("test");
    }
}
