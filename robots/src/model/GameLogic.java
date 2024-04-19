package model;

import java.util.Observable;

public class GameLogic extends Observable {

    private final Robot robot;

    public GameLogic() {
        this.robot = new Robot(100, 100);
    }

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.003;

    public String getRobotInfo() {
        return this.robot.getInfo();
    }

    public Point getRobotPosition() {
        return this.robot.getPosition();
    }

    public double getRobotDirection() {
        return this.robot.getDirection();
    }

    public void update(Point target) {

        Point robotPosition = robot.getPosition();
        double robotDirection = robot.getDirection();

        double distance = RobotMath.distance(target, robotPosition);
        if (distance < 0.5) return;

        double angleToTarget = RobotMath.angleTo(robotPosition, target);
        double angleDifference = RobotMath.asNormalizedRadians(angleToTarget - robotDirection);
        double angularVelocity = (angleDifference < Math.PI) ? maxAngularVelocity : -maxAngularVelocity;

        moveRobot(maxVelocity, angularVelocity, 10);

        setChanged();
        notifyObservers("robot moved");
        clearChanged();
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        Point robotPosition = robot.getPosition();
        double robotDirection = robot.getDirection();

        velocity = RobotMath.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = RobotMath.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double newX = robotPosition.x + velocity / angularVelocity *
                (Math.sin(robotDirection + angularVelocity * duration) - Math.sin(robotDirection));

        if (!Double.isFinite(newX)) {
            newX = robotPosition.x + velocity * duration * Math.cos(robotDirection);
        }

        double newY = robotPosition.y - velocity / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * duration) - Math.cos(robotDirection));

        if (!Double.isFinite(newY)) {
            newY = robotPosition.y + velocity * duration * Math.sin(robotDirection);
        }

        robot.setPosition(new Point(newX, newY));
        double newDirection = RobotMath.asNormalizedRadians(robotDirection + angularVelocity * duration);
        robot.setDirection(newDirection);
    }
}