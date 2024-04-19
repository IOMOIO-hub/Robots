package gui;

import state.WindowWithState;

import java.awt.*;
import javax.swing.JPanel;

import model.GameLogic;

public class GameWindow extends WindowWithState
{
    private final GameVisualizer visualizer;
    public GameWindow(GameLogic gameLogic)
    {
        super("Игровое поле");
        visualizer = new GameVisualizer(gameLogic);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
