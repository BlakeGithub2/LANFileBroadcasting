package main.browse.viewown;

import main.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class BaseFileUnitTest extends ApplicationTest {
    @TempDir
    public static File baseFolder;

    @BeforeEach
    public void beforeEach() throws Exception {
        createEmptyBaseFolder();

        Main.activateTest(baseFolder);
        launch(Main.class);

        WaitForAsyncUtils.waitForFxEvents();
    }

    private void createEmptyBaseFolder() {
        int counter = 0;
        baseFolder = new File("test");

        if (baseFolder.exists()) {
            try {
                deleteTestBaseFile();
            } catch (IOException e) {
                System.out.println("WARNING: TEST BASE FOLDER ALREADY EXISTS. CREATING NEW FOLDER.");
            }
        }

        while (baseFolder.exists()) {
            baseFolder = new File("test" + counter);
            counter++;
        }

        baseFolder.mkdir();
    }

    private static void deleteTestBaseFile() throws IOException {
        File projectsFile = new File(baseFolder.getPath() + "/projects");

        String[] projectNames = projectsFile.list();

        if (projectNames != null) {
            for (String name : projectNames) {
                String projectFilePath = projectsFile.getPath() + "/" + name;
                File projectFile = new File(projectFilePath);
                File projectInfo = new File(projectFile + "/" + Main.PROJECT_INFO_FILE_PATH);

                System.gc();
                Files.delete(projectInfo.toPath());
                Files.delete(projectFile.toPath());
            }
            Files.delete(projectsFile.toPath());
        }

        try {
            Files.delete(baseFolder.toPath());
        } catch (Exception e) {
            throw new IOException("WARNING: Could not delete base folder.");
        }
    }

    @AfterEach
    public void afterEach() throws IOException {
        deleteTestBaseFile();
    }
}
