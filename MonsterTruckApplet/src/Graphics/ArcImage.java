package Graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;

import Auxiliary.IArcImage;

public class ArcImage implements IArcImage {

    private final int penWidth = 2;
    private final Stroke stroke = new BasicStroke(penWidth,
	    BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

    private static ArcImage instance = null;

    private ArcImage() {
    };

    public static ArcImage getInstance() {
	if (instance == null)
	    instance = new ArcImage();
	return instance;
    }

    @Override
    public Image getImage(double radius, double startAngle, double arcAngle) {
	Bounds bounds = Bounds.getBounds(radius, startAngle, arcAngle);
	int width = (int) (bounds.xMax - bounds.xMin);
	int height = (int) (bounds.yMax - bounds.yMin) + 500;
	int offsetX = (int) (bounds.xMin);
	int offsetY = (int) (bounds.yMin);
	Image image = new Image(width, height, offsetX, offsetY);
	Graphics2D graphics = image.createGraphics();
	graphics.setColor(GameColors.GREY_DARKER);
	graphics.fillRect(0, (int) 0, width, height);
	Composite compositePrevious = graphics.getComposite();
	graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
	graphics.fillOval(-offsetX - (int) radius, -offsetY - (int) radius,
		(int) (2 * radius), (int) (2 * radius));
	graphics.setColor(GameColors.GREY);
	graphics.setComposite(compositePrevious);
	graphics.setStroke(stroke);
	graphics.drawArc(-offsetX - (int) radius - penWidth / 4,
		-offsetY - (int) radius - penWidth / 4,
		(int) (2 * radius) + penWidth / 2,
		(int) (2 * radius) + penWidth / 2, (int) startAngle,
		(int) (arcAngle));
	return image;
    }

}
