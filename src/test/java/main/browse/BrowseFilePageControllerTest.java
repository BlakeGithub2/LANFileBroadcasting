package main.browse;

import javafx.application.Platform;
import main.Main;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrowseFilePageControllerTest extends ApplicationTest {
    private static BrowseFilePageController controller;

    @TempDir
    public static File baseFolder;

    @Before
    public void before() throws IOException {
    }

    @BeforeAll
    public static void beforeEach() throws Exception {
        baseFolder = new File("test");
        baseFolder.mkdir();

        Main.activateTest(baseFolder);
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

    @AfterAll
    public static void after() {
        File projectsFile = new File(baseFolder + "/projects");
        String[] projectNames = projectsFile.list();

        for (String name : projectNames) {
            String internalFilePath = projectsFile.getPath() + "/" + name;
            File internalFile = new File(internalFilePath);

            internalFile.delete();
        }
        projectsFile.delete();

        boolean deleted = baseFolder.delete();
        if (!deleted) {
            System.out.println("COULD NOT DELETE TEST BASE FOLDER>");
        }
    }
}