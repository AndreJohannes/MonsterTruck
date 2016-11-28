package WorldObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Auxiliary.CollisionDynamicObjectBase;
import Auxiliary.Vector2D;
import Graphics.GameColors;

public class Cylinder extends CylinderBase<Graphics> {

    private Image image;

    public Cylinder(Vector2D center, double radius) {
	super(center, radius);
	this.image = getImage(radius);
    }

    @Override
    public void draw(Graphics graphics, double[] state, double offsetX) {
	draw(graphics, new Vector2D(state[offset] - offsetX, state[offset + 1]),
		state[offset + 4]);
    }

    private void draw(Graphics gr, Vector2D location, double angle) {
	if (image != null) {
	    AffineTransform affineTransform = new AffineTransform();
	    affineTransform.rotate(angle, radius, radius);
	    AffineTransformOp affineTransformOp = new AffineTransformOp(
		    affineTransform, null);
	    gr.drawImage(affineTransformOp.filter((BufferedImage) image, null),
		    (int) (location.X - radius), (int) (location.Y - radius),
		    null);
	} else {
	    gr.setColor(GameColors.WINE);
	    gr.fillOval((int) (location.X - radius),
		    (int) (location.Y - radius), (int) (2 * radius),
		    (int) (2 * radius));
	}
    }

    private Image getImage(double radius) {
	Image image = null;
	try {
	    image = ImageIO.read(new File("resources/sphere.png"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	BufferedImage bufferedImage = new BufferedImage((int) (2 * radius),
		(int) (2 * radius), BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics = bufferedImage.createGraphics();
	if (image != null) {
	    graphics.drawImage(image, 0, 0, (int) (2 * radius),
		    (int) (2 * radius), 0, 0, image.getWidth(null),
		    image.getHeight(null), null);
	} else {
	    graphics.setColor(GameColors.WINE);
	    graphics.fillOval(0, 0, (int) (2 * radius), (int) (2 * radius));
	    graphics.setColor(Color.BLACK);
	    graphics.drawLine(0, (int) radius, (int) (radius / 4),
		    (int) radius);
	}
	return bufferedImage;
    }


}
