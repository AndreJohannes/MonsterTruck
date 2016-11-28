import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Auxiliary.IWorldStaticObject;
import Graphics.TextureAtlas;
import XMLParser.XMLWorldFileReader;

public class LandscapeExporter {

    public void export() {
	System.setProperty("user.dir", System.getProperty("user.dir") + "/src");
	XMLWorldFileReader reader = new XMLWorldFileReader(new TextureAtlas());
	BufferedImage bImg = new BufferedImage(7500, 720,
		BufferedImage.TYPE_INT_ARGB);
	Graphics2D cg = bImg.createGraphics();
	for (IWorldStaticObject<Graphics> object : reader
		.getStaticObjectList()) {
	    object.draw(cg, 0);
	}
	try {
	    if (ImageIO.write(bImg, "png", new File("landscape.png"))) {
		System.out.println("-- saved");
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void exportText() {
	BufferedImage bImg = new BufferedImage(200, 200,
		BufferedImage.TYPE_INT_ARGB);
	Font font = new Font("TimesRoman", Font.PLAIN, 18);
	bImg.getGraphics().setFont(font);
	bImg.getGraphics().drawString("Score: 0123456789", 20, 20);
	bImg.getGraphics().drawString("Precollision count: ", 20, 40);
	bImg.getGraphics().drawString("Missed frames: ", 20, 60);
	try {
	    if (ImageIO.write(bImg, "png", new File("text.png"))) {
		System.out.println("-- saved");
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	new LandscapeExporter().exportText();
	;

    }

}
