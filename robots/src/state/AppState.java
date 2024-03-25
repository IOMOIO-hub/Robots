package state;

import javax.swing.*;
import java.io.Serializable;

public class AppState implements Serializable {
    private final WindowState gameWindowState;
    private final WindowState logWindowState;

    public AppState(JInternalFrame gameWindow, JInternalFrame logWindow) {
        logWindowState = new WindowState(logWindow);
        gameWindowState = new WindowState(gameWindow);
    }

    public WindowState getGameWindowState() { return gameWindowState; }
    public WindowState getLogWindowState() { return logWindowState; }
}
