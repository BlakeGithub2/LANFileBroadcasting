package main.browse;

import javafx.stage.Stage;
import main.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrowseFilePageControllerTest extends ApplicationTest {
    private static BrowseFilePageController controller;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Main.prepareTest(new Stage());
        controller = new BrowseFilePageController();
        controller.initialize(null, null);
        System.out.println("initialized.");
    }

    @BeforeAll
    public static void before() throws Exception {
        launch(Main.class);
    }

    @Test
    public void testProjectsListEmpty() {
        assertTrue(controller.getPage().getProjects().size() == 0);
    }
    @Test
    public void testAddProject() {
        //BrowseFilePage page = controller.getPage();
    }
}