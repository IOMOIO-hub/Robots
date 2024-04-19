package gui;

import model.GameLogic;
import model.Point;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import java.util.Observer;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel implements Observer
{
    private volatile Point robotPosition = new Point(100, 100);
    private volatile double robotDirection = 0; 

    private volatile Point target = new Point(150, 100);

    private final GameLogic gameLogic;
    
    public GameVisualizer(GameLogic gameLogic)
    {
        this.gameLogic = gameLogic;
        this.gameLogic.addObserver(this);

        Timer timer = new Timer("events generator", true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameLogic.update(target);
                onRedrawEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTarget(new Point(e.getPoint()));
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void setTarget(Point target) {
        this.target = target;
    }
    
    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void update(Observable o, Object arg){
        if (o.equals(gameLogic))
            if (arg.equals("robot moved")){
                robotPosition = this.gameLogic.getRobotPosition();
                robotDirection = this.gameLogic.getRobotDirection();
            }
    }
    
    private static int round(double value) {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, round(robotPosition.x), round(robotPosition.y), robotDirection);
        drawTarget(g2d, (int) target.x, (int) target.y);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = Math.min(getWidth() - 5, Math.max(5, round(x)));
        int robotCenterY = Math.min(getHeight() - 5, Math.max(5, round(y)));

        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }
    
    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
