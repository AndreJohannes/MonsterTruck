package Auxiliary;

import java.awt.image.BufferedImage;

public interface IArcImage {

    public class Image extends BufferedImage {

	private final int offsetVertical;
	private final int offsetHorizontal;

	public Image(int width, int height, int offsetHorizontal,
		int offsetVertical) {
	    super(width, height, BufferedImage.TYPE_INT_ARGB);
	    this.offsetHorizontal = offsetHorizontal;
	    this.offsetVertical = offsetVertical;
	}

	public int getOffsetVertical() {
	    return offsetVertical;
	}

	public int getOffsetHorizontal() {
	    return offsetHorizontal;
	}

    }

    public static class Bounds {
	public final double xMin;
	public final double yMin;
	public final double xMax;
	public final double yMax;

	public Bounds(double xMin, double yMin, double xMax, double yMax) {
	    this.xMin = xMin;
	    this.yMin = yMin;
	    this.xMax = xMax;
	    this.yMax = yMax;
	}

	public static Bounds getBounds(double radius, double startAngle,
		double arcAngle) {
	    return _getBounds(radius,
		    arcAngle > 0 ? startAngle : startAngle + arcAngle,
		    arcAngle > 0 ? arcAngle : -arcAngle);
	}

	private static Bounds _getBounds(double radius, double startAngle,
		double arcAngle) {
	    double startX = radius * Math.cos(Math.toRadians(startAngle));
	    double startY = -radius * Math.sin(Math.toRadians(startAngle));
	    double endX = radius
		    * Math.cos(Math.toRadians(startAngle + arcAngle));
	    double endY = -radius
		    * Math.sin(Math.toRadians(startAngle + arcAngle));
	    double xMin = Math.min(startX, endX);
	    double yMin = Math.min(startY, endY);
	    double xMax = Math.max(startX, endX);
	    double yMax = Math.max(startY, endY);
	    int iStart = (int) (startAngle - MathHelpers.mod(startAngle, 90)
		    + 90);
	    for (int i = iStart; i < startAngle + arcAngle; i += 90) {
		double x = radius * Math.cos(Math.toRadians(i));
		double y = -radius * Math.sin(Math.toRadians(i));
		xMin = Math.min(xMin, x);
		yMin = Math.min(yMin, y);
		xMax = Math.max(xMax, x);
		yMax = Math.max(yMax, y);
	    }
	    return new Bounds(xMin, yMin, xMax, yMax);
	}
    }

    public Image getImage(double radius, double startAngle, double arcAngle);

}
