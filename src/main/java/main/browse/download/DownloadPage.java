package main.browse.download;

import connections.tcp.TCPClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import main.Main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
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

    public void downloadProject(String projectName) {
        if (projectName == null) {
            showNoPullProjectAlert();
            return;
        }

        File downloadedDir = null;
        try {
            downloadedDir = Main.getBaseFile().getDirectoryAt("downloads");
        } catch (IOException e) {
            showCannotCreateDirectoryAlert();
            return;
        }

        File proposedFile = new File(downloadedDir.getPath() + "/" + projectName);
        if (proposedFile.exists()) {
            showProjectAlreadyExistsAlert();
            return;
        }

        // Here, we know proposed project file is unique and findable
        try {
            String response = (String) client.sendInstruction("download " + projectName);
            System.out.println(response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            showCouldNotDownloadProjectAlert();
        }
    }
    private void showNoPullProjectAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText("Could not start download, as no project was selected.");
        a.show();
    }
    private void showCannotCreateDirectoryAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText("Could not create directory to store downloaded projects in.");
        a.show();
    }
    private void showProjectAlreadyExistsAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText("Project already exists locally.");
        a.show();
    }
    private void showCouldNotDownloadProjectAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText("Could not download project.");
        a.show();
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
