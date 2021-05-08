package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage mainWindow;

    public static int SCREEN_WIDTH = 620;
    public static int SCREEN_HEIGHT = 480;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;
        mainWindow.setTitle("Peer to Peer Version Control");

        showConnectScreen();
    }

    private void showConnectScreen() {
        ConnectPage page = new ConnectPage();
        Scene scene = page.getScene();

        mainWindow.setScene(scene);
        mainWindow.show();
    }
}
