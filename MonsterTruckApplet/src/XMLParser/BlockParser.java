package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldDynamicObject;
import Graphics.TextureAtlas;
import WorldObjects.Block;

public class BlockParser extends BlockParserBase<Graphics> {

    private final TextureAtlas textureAtlas;
    private final String name;

    public BlockParser(TextureAtlas textureAtlas, String name) {
	this.textureAtlas = textureAtlas;
	this.name = name;
    }

    @Override
    public IWorldDynamicObject<Graphics> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return new Block(textureAtlas, name, center, width, height);
    }

}
