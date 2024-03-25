package state;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class WindowState implements Serializable {
    private final Dimension size;
    private final Point location;
    private final boolean isIcon;

    public WindowState(JInternalFrame window) {
        this.size = window.getSize();
        this.location = window.getLocation();
        this.isIcon = window.isIcon();
    }

    public Point getLocation() { return location; }
    public Dimension getSize() { return size; }
    public boolean getIsIcon() { return isIcon; }
}
