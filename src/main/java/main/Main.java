package main;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
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

    public static String GET_DOWNLOADS_COMMAND = "GET_DOWNLOADS";

    public static void main(String[] args) {
        baseFile = new BaseFile();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        sceneController = new SceneController(primaryStage);
        addPages();
        goToFirstScene();
    }

    public static void activateTest(File file) {
        baseFile = new BaseFile(file);
    }

    private static ArrayList<String> initializePageList() {
        ArrayList<String> pageList = new ArrayList<>();
        pageList.add("connect");
        pageList.add("browse");
        pageList.add("installer");
        pageList.add("download");

        return pageList;
    }
    private static void goToFirstScene() {
        if (baseFile.addressFileExists()) {
            sceneController.activate("connect");
        } else {
            sceneController.activate("installer");
        }
    }
    private static void addPages() {
        for (String page : pageList) {
            sceneController.addPage(page);
        }
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

    public static void setBaseFile(File file) {
        Main.baseFile = new BaseFile(file);
    }
}
