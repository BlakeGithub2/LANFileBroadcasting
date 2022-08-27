package main.browse.viewown;

import javafx.application.Platform;
import main.Main;
import main.browse.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class BrowseOwnFilePageTest extends BaseFileUnitTest {
    private static BrowseOwnFilePageController controller;

    @BeforeEach
    public void beforeEach() throws Exception {
        super.beforeEach();
        Platform.runLater(() -> {
            controller = new BrowseOwnFilePageController();
        });
    }

    @Test
    public void testAddProjectSaved() throws IOException {
        File newFile = new File(baseFolder + "/test");
        newFile.createNewFile();

        BrowseOwnFilePage page = controller.getPage();
        try {
            page.addProject(new File(newFile.getPath()));
        } catch (FileNotFoundException e) {
            fail();
        }

        page.save();
        page.load();

        assertEquals(1, page.getUnsavedProjects().size());
        assertEquals("test", page.getUnsavedProjects().get(0).getName());
        assertEquals(baseFolder.getAbsoluteFile() + "\\test",
                page.getUnsavedProjects().get(0).getFilePath().toString());

        newFile.delete();
    }

    @Test
    public void testAddProjectsSaved() throws IOException {
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
    @Test
    public void testAddDuplicateProjectsSaved() throws IOException {
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

        page.save();
        page.load();

        assertEquals(1, findNumInstancesOfProject("test0"));
        assertEquals(1, findNumInstancesOfProject("test1"));
        assertEquals(1, findNumInstancesOfProject("test2"));
        assertEquals(1, findNumInstancesOfProject("test3"));
        assertEquals(1, findNumInstancesOfProject("test4"));

        for (File file : files) {
            file.delete();
        }
    }
    @Test
    public void testAddDuplicateProjectsAfterLoad() throws IOException {
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

        page.save();
        page.load();

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
    // TODO: Same as BrowseFilePageTest.java. Refactor?
    private int findNumInstancesOfProject(String projectName) {
        BrowseOwnFilePage page = controller.getPage();

        int instances = 0;
        for (Project project : page.getUnsavedProjects()) {
            if (project.getName().equals(projectName)) {
                instances++;
            }
        }

        return instances;
    }

    @Test
    public void testDeleteProjectSaved() throws IOException {
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

        page.save();
        page.load();

        page.deleteProject("test0");
        page.deleteProject("test1");
        page.deleteProject("test3");

        assertFalse(page.contains("test0"));
        assertFalse(page.contains("test1"));
        assertFalse(page.contains("test3"));

        assertTrue(page.contains("test2"));
        assertTrue(page.contains("test4"));

        for (File file : files) {
            file.delete();
        }
    }

    @Test
    public void testAddProjectNoBaseFile() throws IOException {
        File newFile = new File(baseFolder + "/test");
        newFile.createNewFile();

        Main.setBaseFile(new File(baseFolder + "/nonexistent"));

        BrowseOwnFilePage page = controller.getPage();
        try {
            page.addProject(new File(newFile.getPath()));
        } catch (FileNotFoundException e) {
            fail();
        }
        assertThrows(FileNotFoundException.class,
                ()->{
                    page.save();
                });
        assertThrows(FileNotFoundException.class,
                ()->{
                    page.load();
                });

        newFile.delete();
    }
}