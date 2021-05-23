package main.browse;

import javafx.application.Platform;
import main.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class BrowseFilePageControllerTest extends ApplicationTest {
    private static BrowseFilePageController controller;

    @TempDir
    public static File baseFolder;

    @BeforeAll
    public static void beforeAll() {
        int counter = 0;
        baseFolder = new File("test");

        if (baseFolder.exists()) {
            System.out.println("WARNING: TEST BASE FOLDER ALREADY EXISTS. CREATING NEW FOLDER.");
        }

        while (baseFolder.exists()) {
            baseFolder = new File("test" + counter);
            counter++;
        }
    }

    @BeforeEach
    public void beforeEach() throws Exception {
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

        page.save();
        page.load();

        assertEquals(1, page.getProjects().size());
        assertEquals("test", page.getProjects().get(0).getName());
        assertEquals(baseFolder.getAbsoluteFile() + "\\test",
                page.getProjects().get(0).getFilePath().toString());

        newFile.delete();
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
        LinkedList<File> files = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            File newFile = new File(baseFolder + "/test" + i);
            newFile.createNewFile();
            files.add(newFile);

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

        for (File file : files) {
            file.delete();
        }
    }
    @Test
    public void testAddProjectsSaved() throws IOException {
        BrowseFilePage page = controller.getPage();
        LinkedList<File> files = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            File newFile = new File(baseFolder + "/test" + i);
            newFile.createNewFile();
            files.add(newFile);

            page.addProject(newFile);

            try {
                page.addProject(new File(newFile.getPath()));
            } catch (FileNotFoundException e) {
                fail();
            }
        }

        page.save();
        page.load();

        assertTrue(page.contains("test0"));
        assertTrue(page.contains("test1"));
        assertTrue(page.contains("test2"));
        assertTrue(page.contains("test3"));
        assertTrue(page.contains("test4"));

        for (File file : files) {
            file.delete();
        }
    }

    @AfterEach
    public void afterEach() throws IOException {
        deleteTestBaseFile();
    }

    private static void deleteTestBaseFile() throws IOException {
        File projectsFile = new File(baseFolder.getPath() + "/projects");
        String[] projectNames = projectsFile.list();

        for (String name : projectNames) {
            String projectFilePath = projectsFile.getPath() + "/" + name;
            File projectFile = new File(projectFilePath);
            File projectInfo = new File(projectFile + "/" + Main.PROJECT_INFO_FILE_PATH);

            System.gc();
            Files.delete(projectInfo.toPath());
            Files.delete(projectFile.toPath());
        }
        Files.delete(projectsFile.toPath());

        boolean deleted = baseFolder.delete();
        if (!deleted) {
            throw new IOException("WARNING: COULD NOT DELETE TEST BASE FOLDER.");
        }
    }
}