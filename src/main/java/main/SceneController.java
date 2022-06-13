package main;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;

public class SceneController {
    // Thanks to https://stackoverflow.com/questions/37200845/how-to-switch-scenes-in-javafx!
    private HashMap<String, Scene> sceneMap = new HashMap<>();
    private HashMap<String, Object> transferredData = new HashMap<>();
    private Stage mainWindow;
    private String currentScene;

    public SceneController(Stage primaryStage) {
        mainWindow = primaryStage;
        mainWindow.setTitle("Peer to Peer Version Control");

        // Terminate program when window closes
        mainWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void addPage(String name) {
        Scene scene = loadScene(name);
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be loaded.");
        }
        sceneMap.put(name, scene);
    }

    private Scene loadScene(String loadText) {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/scenes/" + loadText + ".fxml"));
            return new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        } catch (IOException e) {
            System.out.println("Exception loading login page scene. Attempted to load: " + loadText);
            e.printStackTrace();
            return null;
        }
    }

    public void activate(String name) {
        Scene scene = sceneMap.get(name);

        if (!sceneMap.containsKey(name)) {
            throw new IllegalArgumentException("Scene with that name has not been registered in sceneMap.");
        }

        currentScene = name;
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    public void addDataToTransfer(String name, Object obj) {
        transferredData.put(name, obj);
    }

    public Object getTransferredData(String name) {
        return transferredData.get(name);
    }
    public HashMap<String, Object> getTransferredData() {
        return transferredData;
    }

    public String getCurrentScene() {
        return currentScene;
    }

    public Scene getScene(String name) {
        return sceneMap.get(name);
    }

    public Stage getMainWindow() {
        return mainWindow;
    }
}
