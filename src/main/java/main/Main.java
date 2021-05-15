package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.browse.BrowseFilePage;
import main.browse.BrowseFilePageController;
import main.connectpage.ConnectPage;
import main.connectpage.ConnectPageController;

public class Main extends Application {

    private static SceneController sceneController;

    public static int SCREEN_WIDTH = 620;
    public static int SCREEN_HEIGHT = 480;

    public static String IMAGE_PACKAGE = "/sprites/";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        sceneController = new SceneController(primaryStage);

        addPages();
        addModels();

        sceneController.activate("connectcode");
    }

    private void addPages() {
        sceneController.addPage("connectcode");
        sceneController.addPage("browse");
    }

    private void addModels() {
        // TODO: Find a way to automatically do this so package structure is correct
        ConnectPageController.addModel(new ConnectPage());
        BrowseFilePageController.addModel(new BrowseFilePage());
    }

    public static SceneController getSceneController() {
        return sceneController;
    }
}
