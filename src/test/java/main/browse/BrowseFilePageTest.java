package main.browse;

import javafx.application.Platform;
import main.browse.viewown.BaseFileUnitTest;
import main.browse.viewown.BrowseOwnFilePage;
import main.browse.viewown.BrowseOwnFilePageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class BrowseFilePageTest extends BaseFileUnitTest {
    private static BrowseOwnFilePageController controller;

    @BeforeEach
    public void beforeEach() throws Exception {
        super.beforeEach();
        Platform.runLater(() -> {
            controller = new BrowseOwnFilePageController();
        });
    }

    @Test
    public void testProjectsListEmpty() {
        assertEquals(0, controller.getPage().getProjects().size());
    }
    @Test
    public void testAddProject() throws IOException {
        File newFile = new File(baseFolder + "/test");
        newFile.createNewFile();

        BrowseOwnFilePage page = controller.getPage();
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
    public void testAddCorruptedProject() {
        BrowseOwnFilePage page = controller.getPage();
        try {
            page.addProject(new File(baseFolder + "/nonexistent"));
        } catch (FileNotFoundException e) {}

        assertEquals(0, page.getProjects().size());
    }
    @Test
    public void testAddProjects() throws IOException {
        BrowseOwnFilePage page = controller.getPage();
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
    public void testAddDuplicateProjects() throws IOException {
        BrowseOwnFilePage page = controller.getPage();
        ArrayList<File> files = new ArrayList<>();

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

        page.addProject(files.get(0));
        page.addProject(files.get(1));
        page.addProject(files.get(2));
        page.addProject(files.get(2));

        assertEquals(1, findNumInstancesOfProject("test0"));
        assertEquals(1, findNumInstancesOfProject("test1"));
        assertEquals(1, findNumInstancesOfProject("test2"));
        assertEquals(1, findNumInstancesOfProject("test3"));
        assertEquals(1, findNumInstancesOfProject("test4"));

        for (File file : files) {
            file.delete();
        }
    }

    private int findNumInstancesOfProject(String projectName) {
        BrowseOwnFilePage page = controller.getPage();

        int instances = 0;
        for (Project project : page.getProjects()) {
            if (project.getName().equals(projectName)) {
                instances++;
            }
        }

        return instances;
    }

    @Test
    public void testDeleteProject() throws IOException {
        BrowseOwnFilePage page = controller.getPage();
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

        page.deleteProject("test0");
        page.deleteProject("test1");
        page.deleteProject("test3");

        assertFalse(page.contains("test0"));
        assertFalse(page.contains("test1"));
        assertFalse(page.contains("test3"));

        assertTrue(page.contains("test2"));
        assertTrue(page.contains("test4"));

        for (File file : files) {
            assertTrue(file.exists());
            file.delete();
        }
    }

    @Test
    public void testDeleteNonexistentProject() {
        BrowseOwnFilePage page = controller.getPage();

        assertThrows(FileNotFoundException.class,
                ()->{
                    page.deleteProject("nonexistent");
                });
    }
}