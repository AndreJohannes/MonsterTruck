package Graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Landscape implements IBackground<Graphics> {

    private Image image;

    public Landscape(TextureAtlas textureAtlas) {
	int width = (int) (textureAtlas.landscape.scale * (2048));
	image = new BufferedImage(width, 720, BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics = ((BufferedImage) image).createGraphics();
	graphics.setRenderingHint(
		RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	graphics.drawImage(textureAtlas.getTextureImage(), 0, 0,
		width, 720, (int) 0, (int) (int) textureAtlas.landscape.area.y1,
		2048, (int) textureAtlas.landscape.area.y2, null);
    }

    @Override
    public void draw(Graphics graphics, double offset) {
	graphics.drawImage(image, (int) -offset, 0, null);
    }

}
