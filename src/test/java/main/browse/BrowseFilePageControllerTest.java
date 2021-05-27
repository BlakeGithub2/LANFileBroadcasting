package main.browse;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import main.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

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
    public void testAddProject() {
        clickOn(lookup("#addButton").queryButton());
        press(KeyCode.N);
        press(KeyCode.ENTER);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //clickOn(lookup("#nonexistentFileAlertOkButton").queryButton());
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