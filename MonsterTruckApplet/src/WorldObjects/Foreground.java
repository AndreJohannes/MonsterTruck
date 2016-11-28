package WorldObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import Graphics.TextureAtlas;

public class Foreground extends ForegroundBase<Graphics> {

    private Image image;
    private int dx,dy;

    public Foreground(TextureAtlas textureAtlas, String name, Vector2D pointA,
	    Vector2D pointB) {
	super(pointA, pointB);
	int width = (int) Math.abs(pointB.X - pointA.X);
	int height = (int) Math.abs(pointB.Y - pointA.Y);
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	Rect textureRect = textureAtlas.textureByName.get(name);
	image.getGraphics().drawImage(textureAtlas.getTextureImage(), 0, 0,
		width, height, (int) textureRect.x1, (int) textureRect.y1,
		(int) textureRect.x2, (int) textureRect.y2, null);
	dx = (int) Math.min(pointA.X, pointB.X);
	dy = (int) Math.min(pointA.Y, pointB.Y);
    }

    @Override
    public void draw(Graphics graphics, double offset) {
	graphics.drawImage(image, (int) (dx-offset),(int) dy , null);
    }

}
