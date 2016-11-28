package Graphics;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import TextureMap.TextureMap;

public class TextureAtlas extends TextureMap {

    private final Image textureImage;

    public TextureAtlas() {
	try {
	    textureImage = ImageIO.read(new File("resources/texturemap.png"));
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    public Image getTextureImage() {
	return textureImage;
    }

}
