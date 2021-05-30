package main.browse;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import main.Main;
import main.utils.TestFXUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrowseFilePageControllerTest extends BaseFileUnitTest {
    private static BrowseFilePageController controller;
    private TestFXUtils utils = new TestFXUtils();

    @Test
    public void testProjectsListEmpty() {
        assertEquals(0, lookup("#projectList").queryListView().getItems().size());
    }

    @Test
    public void testBackToMainMenu() {
        clickOn(lookup("#backButton").queryButton());
        assertEquals("newconnect", Main.getSceneController().getCurrentScene());
    }

    @Test
    public void testCancelAddProject() {
        clickOn(lookup("#addButton").queryButton());
        utils.tap(KeyCode.ESCAPE);
        assertEquals(0, lookup("#projectList").queryListView().getItems().size());
    }

    @Test
    public void testAddNonexistentProject() {
        clickOn(lookup("#addButton").queryButton());

        utils.tap(KeyCode.N);
        utils.tap(KeyCode.ENTER);

        WaitForAsyncUtils.waitForFxEvents();
        // TODO: Check alert text

        utils.tap(KeyCode.ENTER);
        utils.tap(KeyCode.ESCAPE);

        assertEquals(0, lookup("#projectList").queryListView().getItems().size());
    }

    @Test
    public void testAddProject() throws IOException {
        // TODO: Same code in BrowseFilePageTest. Refactor?
        File newFile = new File(baseFolder + "/test");
        newFile.mkdir();

        // Add the project
        clickOn(lookup("#addButton").queryButton());

        utils.inputText(baseFolder.getAbsoluteFile() + "/test");
        utils.tap(KeyCode.ENTER);

        newFile.delete();
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        super.beforeEach();

        Platform.runLater(() -> {
            controller = new BrowseFilePageController();
            Main.getSceneController().activate("browse");
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

}