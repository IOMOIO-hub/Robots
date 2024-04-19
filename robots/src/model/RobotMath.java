package model;

public class RobotMath {

    public static double distance(Point a, Point b) {
        double diffX = a.x - b.x;
        double diffY = a.y - b.y;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public static double angleTo(Point from, Point to) {
        double diffX = to.x - from.x;
        double diffY = to.y - from.y;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public static double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    public static double applyLimits(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
