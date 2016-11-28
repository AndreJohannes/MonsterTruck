package WorldObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import Graphics.GameColors;
import Graphics.TextureAtlas;

public class Block extends BlockBase<Graphics> {

    private Image image;

    public Block(TextureAtlas textureAtlas, String name, Vector2D startPosition,
	    double width, double height) {
	super(startPosition, width, height);
	image = new BufferedImage((int) width, (int) height,
		BufferedImage.TYPE_INT_ARGB);
	Rect textureRect = textureAtlas.textureByName.get(name);
	image.getGraphics().drawImage(textureAtlas.getTextureImage(), 0, 0,
		(int) width, (int) height, (int) textureRect.x1,
		(int) textureRect.y1, (int) textureRect.x2,
		(int) textureRect.y2, null);
    }

    @Override
    public void draw(Graphics graphics, double[] state, double xOffset) {
	Vector2D center = new Vector2D(state[offset] - xOffset,
		state[offset + 1]);
	double rad = state[offset + 4];
	double sin = Math.sin(rad);
	double cos = Math.cos(rad);
	Polygon polygon = new Polygon();
	double cosWidth = cos * (width / 2.0 + 1);
	double cosHeight = cos * (height / 2.0 + 1);
	double sinWidth = sin * (width / 2.0 + 1);
	double sinHeight = sin * (height / 2.0 + 1);
	polygon.addPoint((int) (center.X - cosWidth - sinHeight),
		(int) (center.Y - cosHeight + sinWidth));
	polygon.addPoint((int) (center.X - cosWidth + sinHeight),
		(int) (center.Y + cosHeight + sinWidth));
	polygon.addPoint((int) (center.X + cosWidth + sinHeight),
		(int) (center.Y + cosHeight - sinWidth));
	polygon.addPoint((int) (center.X + cosWidth - sinHeight),
		(int) (center.Y - cosHeight - sinWidth));
	graphics.setColor(_collisionObject.atRest() ? GameColors.Green : GameColors.WINE);
	graphics.fillPolygon(polygon);
	AffineTransform trans = new AffineTransform();
	trans.translate(boundingRadius, boundingRadius);
	trans.rotate(-rad);
	trans.translate(-25 / 2., -75 / 2.);
	AffineTransformOp affineTransformOp = new AffineTransformOp(trans,
		AffineTransformOp.TYPE_BILINEAR);
	((Graphics2D) graphics).drawImage(
		affineTransformOp.filter((BufferedImage) image, null),
		(int) (center.X - boundingRadius),
		(int) (center.Y - boundingRadius), null);
    }

}
