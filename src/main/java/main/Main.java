package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.connectpage.ConnectPage;
import main.connectpage.ConnectPageController;
import main.install.InstallPage;
import main.install.InstallPageController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private static SceneController sceneController;

    public static int SCREEN_WIDTH = 620;
    public static int SCREEN_HEIGHT = 480;

    public static String MAIN_FILE_NAME = "LANVersionControl";
    public static String PROJECT_INFO_FILE_PATH = "projectinfo.txt";
    public static String LOCAL_ADDRESS_FILE_PATH = "basefile.txt";

    public static String IMAGE_PACKAGE = "/sprites/";

    private static BaseFile baseFile;
    private static ArrayList<String> pageList = initializePageList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        sceneController = new SceneController(primaryStage);
        baseFile = new BaseFile();

        if (baseFile.addressFileExists()) {
            baseFile.loadBasePath();
        }

        addPages();
        addModels();

        goToFirstScene();
    }
    public static void prepareTest(Stage primaryStage) throws IOException {
        sceneController = new SceneController(primaryStage);
        baseFile = new BaseFileTest();
        ((BaseFileTest) baseFile).setFile(new File("test"));

    }

    private static ArrayList<String> initializePageList() {
        ArrayList<String> pageList = new ArrayList<>();
        pageList.add("newconnect");
        pageList.add("browse");
        pageList.add("installer");

        return pageList;
    }
    private void goToFirstScene() {
        if (baseFile.addressFileExists()) {
            sceneController.activate("newconnect");
        } else {
            sceneController.activate("installer");
        }
    }
    private static void addPages() {
        for (String page : pageList) {
            sceneController.addPage(page);
        }
    }
    private static void addModels() {
        // Register all pages here!
        // Necessary because otherwise controller under construction
        // will load page while page is being created.

        ConnectPageController.addModel(new ConnectPage());
        InstallPageController.addModel(new InstallPage());
    }

    public static SceneController getSceneController() {
        return sceneController;
    }

    public static BaseFile getBaseFile() {
        return baseFile;
    }

    public static ArrayList<String> getPageList() {
        return pageList;
    }
}
