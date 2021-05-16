package main;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public abstract class GUIPaneList {
    // Variables
    private ArrayList<GUIPane> paneElements;
    private Pane pane;

    // Constructors
    public GUIPaneList(Pane pane) {
        this.pane = pane;
        this.paneElements = new ArrayList<>();
    }

    // Misc.
    protected void add(GUIPane element) {
        // NOTE: protected because lists should add their own adding behavior
        paneElements.add(element);
        addToPane(element);
    }
    protected void addToPane(GUIPane element) {
        if (element == null) {
            throw new NullPointerException("GUI Element cannot be null.");
        }
        if (pane == null) {
            throw new NullPointerException("Pane to GUI Element to cannot be null.");
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pane.getChildren().add(element.getPane());
            }
        });
    }

    // Getters
    public ArrayList<GUIPane> getElementList() {
        return paneElements;
    }

}
