package main.browse;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import main.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrowseFilePageControllerTest extends BaseFileUnitTest {
    private static BrowseFilePageController controller;

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
        press(KeyCode.ESCAPE);
        assertEquals(0, lookup("#projectList").queryListView().getItems().size());
    }

    @Test
    public void testAddNonexistentProject() {
        clickOn(lookup("#addButton").queryButton());

        press(KeyCode.N);
        press(KeyCode.ENTER);

        WaitForAsyncUtils.waitForFxEvents();
        // TODO: Check alert text

        press(KeyCode.ENTER);
        press(KeyCode.ESCAPE);

        assertEquals(0, lookup("#projectList").queryListView().getItems().size());
    }

    @Test
    public void testAddProject() throws IOException {
        // TODO: Same code in BrowseFilePageTest. Refactor?
        File newFile = new File(baseFolder + "/test");
        newFile.createNewFile();

        // Add the project
        clickOn(lookup("#addButton").queryButton());

        inputText(baseFolder.getAbsoluteFile() + "/test");
        press(KeyCode.ENTER);

        newFile.delete();
    }
    private void inputText(String text) {
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            System.out.println(c);
            if (c == ':') {
                press(KeyCode.COLON);
            } else if (c == '\\') {
                press(KeyCode.BACK_SLASH);
            } else {
                press(KeyCode.getKeyCode(c + ""));
            }
        }
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