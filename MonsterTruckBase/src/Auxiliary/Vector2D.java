package Auxiliary;

public class Vector2D {

    public static class Line {

	public final Vector2D A;
	public final Vector2D B;

	public Line(Vector2D A, Vector2D B) {
	    this.A = A;
	    this.B = B;
	};

    }

    public static class Circle {
	public final Vector2D A;
	public final double radius;

	public Circle(Vector2D A, double radius) {
	    this.A = A;
	    this.radius = radius;
	}
    }
    
    public static class Rect {

	    public final float x1, x2, y1, y2;

	    public Rect(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	    }


	}

    public final double X;
    public final double Y;

    public Vector2D(double x, double y) {
	X = x;
	Y = y;
    }

    public Vector2D add(Vector2D other) {
	return new Vector2D(this.X + other.X, this.Y + other.Y);
    }

    public Vector2D add(double x, double y) {
	return new Vector2D(this.X + x, this.Y + y);
    }

    public Vector2D subs(Vector2D other) {
	return new Vector2D(this.X - other.X, this.Y - other.Y);
    }

    public Vector2D scale(double factor) {
	return new Vector2D(factor * this.X, factor * this.Y);
    }

    public double dot(Vector2D other) {
	return this.X * other.X + this.Y * other.Y;
    }

    public double norm() {
	return Math.sqrt(X * X + Y * Y);
    }

    public Vector2D normalise() {
	double norm = Math.sqrt(X * X + Y * Y);
	return new Vector2D(X / norm, Y / norm);
    }

    public double distance(Vector2D other) {
	return Math.sqrt(
		(X - other.X) * (X - other.X) + (Y - other.Y) * (Y - other.Y));
    }

    public Vector2D rotate(double rad) {
	return rotate(Math.sin(rad), Math.cos(rad));
    }

    public Vector2D rotate(double sin, double cos) {
	return new Vector2D(cos * X + sin * Y, cos * Y - sin * X);
    }

    private static Vector2D zeroVector = new Vector2D(0, 0);

    public static Vector2D getZeroVector() {
	return zeroVector;
    }

}
