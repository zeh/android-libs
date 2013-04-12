package com.zehfernando.data.geom;

public final class Point {

	// A point, with special geometry functions
	// TODO: use double instead?

	// Properties
	private double x;
	private double y;


	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public Point() {
		x = 0;
		y = 0;
	}


	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public Point clone() {
		return Point.fromXY(x, y);
	}

	public void subtract(double __value) {
		x -= __value;
		y -= __value;
	}

	public void subtract(Point __point) {
		x -= __point.x();
		y -= __point.y();
	}

	public void add(double __value) {
		x += __value;
		y += __value;
	}

	public void add(Point __point) {
		x += __point.x();
		y += __point.y();
	}

	public void multiply(double __value) {
		x *= __value;
		y *= __value;
	}

	public void multiply(Point __point) {
		x *= __point.x();
		y *= __point.y();
	}

	public void divide(double __value) {
		x /= __value;
		y /= __value;
	}

	public void divide(Point __point) {
		x /= __point.x();
		y /= __point.y();
	}

	public void rotateDegrees(double __angle) {
		// Rotates this point clockwise
		setAngleDegrees(angleDegrees() + __angle);
	}


	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	public static Point fromXY(double __x, double __y) {
		// New cartesian coordinate point
		Point p = new Point();
		p.setX(__x);
		p.setY(__y);
		return p;
	}

	public static Point fromAngleDegreesDistance(double __angle, double __distance) {
		return fromAngleDistance(Point.degreesToRadians(__angle), __distance);
	}

	public static Point fromAngleDistance(double __angle, double __distance) {
		// New polar coordinate point
		// Creates a new Point from an angle (in radians) and a distance
		Point p = new Point();
		p.setX(Math.cos(__angle) * __distance);
		p.setY(Math.sin(__angle) * __distance);
		return p;
	}

	public static double degreesToRadians(double __degrees) {
		return __degrees / 180 * Math.PI;
	}

	public static double radiansToDegrees(double __radians) {
		return (__radians / Math.PI) * 180;
	}

	public static double distance(Point __pointA, Point __pointB) {
//		double dx = __pointA.x - __pointB.x;
//		double dy = __pointA.y - __pointB.y;
//		return Math.sqrt(dx*dx + dy*dy);
		Point p = __pointA.clone();
		p.subtract(__pointB);
		return p.distance();
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------


	public double x() {
		return x;
	}

	public float xFloat() {
		return (float)x;
	}

	public void setX(double __x) {
		x = __x;
	}

	public double y() {
		return y;
	}

	public float yFloat() {
		return (float)y;
	}

	public void setY(double __y) {
		y = __y;
	}

	public double angle() {
		// Angle, in radians
		return Math.atan2(y, x);
	}

	public void setAngleDegrees(double __angle) {
		setAngle(Point.degreesToRadians(__angle));
	}

	public void setAngle(double __angle) {
		// Sets the angle (in radians) without changing the distance
		Point p = Point.fromAngleDistance(__angle, distance());
		setX(p.x());
		setY(p.y());
		return;
	}

	public double angleDegrees() {
		// Angle, in degrees
		return Point.radiansToDegrees(angle());
	}

	public float angleDegreesFloat() {
		// Angle, in degrees
		return (float)angleDegrees();
	}

	public double distance() {
		return Math.sqrt(x*x + y*y);
	}

	public void setDistance(double __distance) {
		// Sets the distance without changing the angle
		Point p = Point.fromAngleDistance(angle(), __distance);
		setX(p.x());
		setY(p.y());
		return;
	}
}
