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
    private Stage mainWindow;

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
            throw new IllegalArgumentException("Scene not found and cannot be added to sceneMap.");
        }

        sceneMap.put(name, scene);
    }

    private Scene loadScene(String loadText) {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/scenes/" + loadText + ".fxml"));
            Scene scene = new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
            return scene;
        } catch (IOException e) {
            System.out.println("Exception loading login page scene. Attempted to load: " + loadText);
            e.printStackTrace();
            return null;
        }
    }

    public void activate(String name) {
        Scene scene = sceneMap.get(name);

        if (scene == null) {
            throw new NullPointerException("Scene not found in sceneMap.");
        }

        mainWindow.setScene(scene);
        mainWindow.show();
    }

    public Scene getScene(String name) {
        return sceneMap.get(name);
    }

    public Stage getMainWindow() {
        return mainWindow;
    }
}
