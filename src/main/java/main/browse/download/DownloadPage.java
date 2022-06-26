package main.browse.download;

import connections.tcp.TCPClientThread;
import main.Main;
import main.browse.BrowseFilePage;
import main.browse.Project;

import java.io.IOException;
import java.util.List;

public class DownloadPage extends BrowseFilePage {
    private TCPClientThread client;

    public List<Project> getDownloadableProjects() {
        try {
            pullTransferredInformation();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public void pullTransferredInformation() throws IOException, ClassNotFoundException {
        client = (TCPClientThread) Main.getSceneController().getTransferredData("tcpClient");
        List<String> result = (List<String>) client.sendInstruction("get downloads");
        System.out.println(result);
    }
}
