package main.browse.download;

import connections.tcp.TCPClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;

import java.io.IOException;
import java.util.List;

public class DownloadPage {
    private TCPClient client;
    private ObservableList<String> downloadableProjects;

    public DownloadPage() {
        this.downloadableProjects = FXCollections.observableArrayList();
    }

    public void findDownloadableProjects() {
        try {
            pullTransferredInformation();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void pullTransferredInformation() throws IOException, ClassNotFoundException {
        downloadableProjects.clear();

        List<String> result = (List<String>) client.sendInstruction("get downloads");

        Platform.runLater(() -> {
            for (String projectName : result) {
                downloadableProjects.add(projectName);
            }
        });
    }

    public void exit() throws Exception {
        Main.getSceneController().activate("connect");
        if (client != null) {
            client.toggle();
        } else {
            throw new Exception("Could not shut down null client.");
        }
    }

    public void onLoad() {
        client = (TCPClient) Main.getSceneController().getTransferredData("tcpClient");
        findDownloadableProjects();
    }

    public ObservableList<String> getProjects() {
        return downloadableProjects;
    }
}
