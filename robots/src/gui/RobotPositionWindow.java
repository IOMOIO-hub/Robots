package gui;

import model.Robot;
import state.WindowWithState;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class RobotPositionWindow extends WindowWithState implements Observer
{
    private final Robot robot;
    private final JTextArea textField;

    public RobotPositionWindow(Robot robot){
        super("Окно состояния робота");
        JPanel panel = new JPanel(new BorderLayout());

        textField = new JTextArea();
        panel.add(textField, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();

        this.robot = robot;
        robot.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable.equals(robot) && arg.equals("robot moved"))
            onRobotMoved();
    }

    private void onRobotMoved() {
        textField.setText(robot.getInfo());
    }
}