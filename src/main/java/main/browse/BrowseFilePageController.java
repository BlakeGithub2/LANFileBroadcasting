package main.browse;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseFilePageController implements Initializable {
    private static BrowseFilePage page;

    public BrowseFilePageController() { }

    public static void goTo(Stage stage) {
        stage.setScene(page.getScene());
        stage.show();
        System.out.println("wee");
    }

    public static void addModel(BrowseFilePage page) {
        BrowseFilePageController.page = page;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}