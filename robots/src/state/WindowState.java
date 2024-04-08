package state;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class WindowState implements Serializable {
    private final Dimension size;
    private final Point location;
    private final boolean isIcon;

    private Serializable additionalInfo;

    public WindowState(JInternalFrame window) {
        this.size = window.getSize();
        this.location = window.getLocation();
        this.isIcon = window.isIcon();
        this.additionalInfo = null;
    }

    public Point getLocation() { return location; }
    public Dimension getSize() { return size; }
    public boolean getIsIcon() { return isIcon; }
    public Serializable getAdditionalInfo() { return additionalInfo; }

    public void setAdditionalInfo(Serializable info) {
        this.additionalInfo = info;
    }
}
