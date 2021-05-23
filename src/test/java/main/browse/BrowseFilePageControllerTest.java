package main.browse;

import javafx.application.Platform;
import main.Main;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrowseFilePageControllerTest extends ApplicationTest {
    private static BrowseFilePageController controller;

    @Rule
    public TemporaryFolder base = new TemporaryFolder();

    @BeforeEach
    public void before() throws Exception {
        Main.activateTest(base.newFile());
        launch(Main.class);
        Platform.runLater(() -> {
            controller = new BrowseFilePageController();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testProjectsListEmpty() {
        System.out.println(Main.getBaseFile());
        assertEquals(0, controller.getPage().getProjects().size());
    }
    @Test
    public void testAddProject() {
        //BrowseFilePage page = controller.getPage();
    }
}