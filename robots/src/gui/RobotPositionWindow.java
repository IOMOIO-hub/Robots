package gui;

import model.GameLogic;
import state.WindowWithState;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class RobotPositionWindow extends WindowWithState implements Observer
{
    private final GameLogic gameLogic;
    private final JTextArea textField;

    public RobotPositionWindow(GameLogic gameLogic){
        super("Окно состояния робота");
        JPanel panel = new JPanel(new BorderLayout());

        textField = new JTextArea();
        panel.add(textField, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();

        this.gameLogic = gameLogic;
        gameLogic.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable.equals(gameLogic) && arg.equals("robot moved"))
            onRobotMoved();
    }

    private void onRobotMoved() {
        textField.setText(this.gameLogic.getRobotInfo());
    }
}