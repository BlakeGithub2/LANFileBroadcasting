package main.browse;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrowseFilePageControllerTest extends BaseFileUnitTest {
    private static BrowseFilePageController controller;

    @Test
    public void testProjectsListEmpty() {
        
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        super.beforeEach();
        Platform.runLater(() -> {
            controller = new BrowseFilePageController();
        });
    }


}