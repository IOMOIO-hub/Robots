package model;

public class Point {

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point(java.awt.Point point) {
        this.x = point.x;
        this.y = point.y;
    }
}
