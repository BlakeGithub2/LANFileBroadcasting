package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.connectpage.ConnectPage;
import main.connectpage.ConnectPageController;

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

        ConnectPageController.addModel(new ConnectPage());
        ConnectPageController.goTo(mainWindow);
    }
}
