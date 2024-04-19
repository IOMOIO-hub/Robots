package model;


public class Robot {

    private volatile Point position;
    private volatile double direction = 0;

    public Robot(int x, int y) {
        super();
        position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }
    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    public double getDirection() {
        return direction;
    }
    public void setDirection(double newDirection) {
        direction = newDirection;
    }

    public String getInfo() {
        return String.format("X: %.2f\nY: %.2f", position.x, position.y);
    }
}