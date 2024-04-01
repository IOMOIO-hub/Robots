package state;

import javax.swing.*;

public abstract class WindowWithState extends JInternalFrame {

    public WindowWithState(String title) {
        super(title, true, true, true, true);
    }

    public WindowState getState() {
        return new WindowState(this);
    }
}