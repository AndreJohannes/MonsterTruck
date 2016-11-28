package WorldObjects;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import Auxiliary.Vector2D;
import Graphics.GameColors;

public class Bar extends BarBase<Graphics> {

    private final BufferedImage bufferedImage;
    private final int penWidth = 2;
    private final Stroke stroke = new BasicStroke(penWidth,
	    BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
	
	public Bar(Vector2D A, Vector2D B) {
		super(A, B);
		bufferedImage = getImage(A, B);
	}

    @Override
    public void draw(Graphics graphics, double offset) {
	graphics.setColor(GameColors.WINE);
	// graphics.drawLine((int) (A.X - offset), (int) A.Y, (int) (B.X -
	// offset),
	// (int) B.Y);
	graphics.drawImage(bufferedImage, (int) (Math.min(A.X, B.X) - offset),
		(int) (Math.min(A.Y, B.Y)), null);
    }

    private BufferedImage getImage(Vector2D A, Vector2D B) {
    	int width = (int) Math.max(Math.abs(A.X - B.X), 1);
    	int height = (int) Math.max(Math.abs(A.Y - B.Y), 1) + 500;
    	BufferedImage imageOverlay = new BufferedImage(width, height,
    		BufferedImage.TYPE_INT_ARGB);
    	double minX = Math.min(A.X, B.X);
    	double minY = Math.min(A.Y, B.Y);
    	Graphics graphics = imageOverlay.getGraphics();
    	Polygon polygon = new Polygon();
    	polygon.addPoint((int) (A.X - minX), (int) (A.Y - minY));
    	polygon.addPoint((int) (B.X - minX), (int) (B.Y - minY));
    	polygon.addPoint(width, height);
    	polygon.addPoint(0, height);
    	graphics.setColor(GameColors.GREY_DARKER);
    	graphics.fillPolygon(polygon);
    	graphics.setColor(GameColors.GREY);
    	((Graphics2D) graphics).setStroke(stroke);
    	graphics.drawLine((int) (A.X - minX), (int) (A.Y + penWidth / 2 - minY),
    		(int) (B.X - minX), (int) (B.Y + penWidth / 2 - minY));
    	return imageOverlay;
        }

   
    
}
