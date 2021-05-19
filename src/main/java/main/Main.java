package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.browse.BrowseFilePage;
import main.browse.BrowseFilePageController;
import main.connectpage.ConnectPage;
import main.connectpage.ConnectPageController;
import main.install.InstallPage;
import main.install.InstallPageController;

public class Main extends Application {

    private static SceneController sceneController;

    public static int SCREEN_WIDTH = 620;
    public static int SCREEN_HEIGHT = 480;
    public static String MAIN_FILE_NAME = "LANVersionControl";
    public static String LOCAL_ADDRESS_FILE_PATH = "basefile.txt";

    public static String IMAGE_PACKAGE = "/sprites/";

    private static BaseFile baseFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        sceneController = new SceneController(primaryStage);
        baseFile = new BaseFile();

        addPages();
        addModels();

        goToFirstScene();
    }

    private void goToFirstScene() {
        if (baseFile.addressFileExists()) {
            sceneController.activate("connectcode");
            baseFile.loadBasePath();
        } else {
            sceneController.activate("installer");
        }
    }

    private void addPages() {
        sceneController.addPage("connectcode");
        sceneController.addPage("browse");
        sceneController.addPage("installer");
    }

    private void addModels() {
        // TODO: Find a way to automatically do this so package structure is correct
        ConnectPageController.addModel(new ConnectPage());
        BrowseFilePageController.addModel(new BrowseFilePage());
        InstallPageController.addModel(new InstallPage());
    }

    public static SceneController getSceneController() {
        return sceneController;
    }

    public static BaseFile getBaseFile() {
        return baseFile;
    }
}
