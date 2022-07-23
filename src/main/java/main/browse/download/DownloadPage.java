package main.browse.download;

import connections.tcp.TCPClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import main.Main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

public class DownloadPage implements PropertyChangeListener {
    private TCPClient client;
    private ObservableList<String> downloadableProjects;

    public DownloadPage() {
        this.downloadableProjects = FXCollections.observableArrayList();
    }

    public void findDownloadableProjects() {
        try {
            pullTransferredInformation();
        } catch (IOException | ClassNotFoundException e) {
            try {
                exit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

    public void exit() throws IOException {
        Main.getSceneController().activate("connect");

        if (client != null) {
            client.stop();
        } else {
            throw new IOException("Could not shut down null client.");
        }
    }

    public void onLoad() {
        client = (TCPClient) Main.getSceneController().getTransferredData("tcpClient");
        client.addObserver(this);
        findDownloadableProjects();
    }

    public ObservableList<String> getProjects() {
        return downloadableProjects;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    exit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showSendBackAlert();
            }
        });
    }

    private void showSendBackAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText("Disconnected");
        a.setContentText("The computer you were connected to has gone offline.");
        a.show();
    }
}
