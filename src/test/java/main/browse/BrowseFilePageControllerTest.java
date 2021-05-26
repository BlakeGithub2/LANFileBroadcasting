package main.browse;

import javafx.application.Platform;
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