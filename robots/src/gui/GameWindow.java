package gui;

import state.WindowState;
import state.WindowWithState;

import java.awt.*;

import javax.swing.JPanel;

public class GameWindow extends WindowWithState
{
    private final GameVisualizer m_visualizer;
    public GameWindow()
    {
        super("Игровое поле");
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public GameWindow(WindowState state) {
        this();
    }
}
