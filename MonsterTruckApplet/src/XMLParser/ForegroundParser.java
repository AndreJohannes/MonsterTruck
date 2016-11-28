package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldNoninteractingObject;
import Graphics.TextureAtlas;
import WorldObjects.Foreground;

public class ForegroundParser extends ForegoundParserBase<Graphics> {

    private final TextureAtlas textureAtlas;
    private final String name;

    public ForegroundParser(TextureAtlas textureAtlas,String name) {
	this.textureAtlas = textureAtlas;
	this.name = name;
    }

    @Override
    public IWorldNoninteractingObject<Graphics> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return new Foreground(textureAtlas, name, pointA, pointB);
    }

}
