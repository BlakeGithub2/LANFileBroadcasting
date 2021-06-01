package main.connectpage;

import javafx.scene.layout.Pane;
import main.GUIPane;
import main.GUIPaneList;

import java.net.InetAddress;

public class ConnectionList extends GUIPaneList {
    // Constructors
    public ConnectionList(Pane pane) {
        super(pane);
    }

    // Misc.
    public boolean containsAddress(InetAddress ip) {
        for (GUIPane element : getElementList()) {
            Connection c = (Connection) element;
            if (c.getAddress() == null) {
                continue;
            }
            if (c.getAddress().equals(ip)) {
                return true;
            }
        }
        return false;
    }
}
