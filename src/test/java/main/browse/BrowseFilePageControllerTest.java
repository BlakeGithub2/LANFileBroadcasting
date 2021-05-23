package main.browse;

import javafx.application.Platform;
import main.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BrowseFilePageControllerTest extends ApplicationTest {
    private static BrowseFilePageController controller;

    @TempDir
    public static File baseFolder;

    @BeforeEach
    public void beforeEach() throws Exception {
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
    public void testAddProject() throws IOException {
        File newFile = new File(baseFolder + "/test");
        newFile.createNewFile();

        BrowseFilePage page = controller.getPage();
        try {
            page.addProject(new File(newFile.getPath()));
        } catch (FileNotFoundException e) {
            fail();
        }

        assertEquals(1, page.getProjects().size());
        assertEquals("test", page.getProjects().get(0).getName());
        assertEquals(baseFolder.getAbsoluteFile() + "\\test",
                page.getProjects().get(0).getFilePath().toString());

        newFile.delete();
    }
    @Test
    public void testAddProjectSaved() throws IOException {
        File newFile = new File(baseFolder + "/test");
        newFile.createNewFile();

        BrowseFilePage page = controller.getPage();
        try {
            page.addProject(new File(newFile.getPath()));
        } catch (FileNotFoundException e) {
            fail();
        }

        page.load();

        assertEquals(1, page.getProjects().size());
        assertEquals("test", page.getProjects().get(0).getName());
        assertEquals(baseFolder.getAbsoluteFile() + "\\test",
                page.getProjects().get(0).getFilePath().toString());
    }

    @Test
    public void testAddCorruptedProject() {
        BrowseFilePage page = controller.getPage();
        try {
            page.addProject(new File(baseFolder + "/nonexistent"));
        } catch (FileNotFoundException e) {}

        assertEquals(0, page.getProjects().size());
    }
    @Test
    public void testAddProjects() throws IOException {
        BrowseFilePage page = controller.getPage();

        for (int i = 0; i < 5; i++) {
            File newFile = new File(baseFolder + "/test" + i);
            newFile.createNewFile();

            try {
                page.addProject(new File(newFile.getPath()));
            } catch (FileNotFoundException e) {
                fail();
            }
        }

        assertTrue(page.contains("test0"));
        assertTrue(page.contains("test1"));
        assertTrue(page.contains("test2"));
        assertTrue(page.contains("test3"));
        assertTrue(page.contains("test4"));
    }
    @Test
    public void testAddProjectsSaved() throws IOException {
        BrowseFilePage page = controller.getPage();

        for (int i = 0; i < 5; i++) {
            File newFile = new File(baseFolder + "/test" + i);
            newFile.createNewFile();

            try {
                page.addProject(new File(newFile.getPath()));
            } catch (FileNotFoundException e) {
                fail();
            }
        }
        
        page.load();

        assertTrue(page.contains("test0"));
        assertTrue(page.contains("test1"));
        assertTrue(page.contains("test2"));
        assertTrue(page.contains("test3"));
        assertTrue(page.contains("test4"));
    }

    @AfterEach
    public void afterEach() {
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
            System.out.println("COULD NOT DELETE TEST BASE FOLDER.");
        }
    }
}