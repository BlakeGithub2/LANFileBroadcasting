package main.browse;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrowseFilePageControllerTest extends ApplicationTest {
    private static BrowseFilePageController controller;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
    }

    @BeforeEach
    public void before() throws Exception {
        launch(Main.class);
        Platform.runLater(() -> {
            controller = new BrowseFilePageController();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testProjectsListEmpty() {
        System.out.println(controller);
        assertTrue(controller.getPage().getProjects().size() == 0);
    }
    @Test
    public void testAddProject() {
        //BrowseFilePage page = controller.getPage();
    }
}