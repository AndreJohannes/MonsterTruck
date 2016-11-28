package Graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Background implements IBackground<Graphics> {

    private Image image;

    public Background(TextureAtlas textureAtlas) {
	image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics = ((BufferedImage) image).createGraphics();
	graphics.setRenderingHint(
		RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	graphics.drawImage(textureAtlas.getTextureImage(), 0, 0,
		1280, 720, (int) textureAtlas.background.x1,
		(int) textureAtlas.background.y1,
		(int) textureAtlas.background.x2,
		(int) textureAtlas.background.y2, null);

    }

    public void draw(Graphics graphics, double offset) {
	if (image != null) {
	    graphics.drawImage(image, (int) -offset, 0, null);
	}
    }

}
