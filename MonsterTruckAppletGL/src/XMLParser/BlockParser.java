package XMLParser;

import Auxiliary.IWorldDynamicObject;
import OpenGL.GLRenderer;
import WorldObjects.Block;

public class BlockParser extends BlockParserBase<GLRenderer> {

    private final GLRenderer renderer;
    private final String name;

    public BlockParser(GLRenderer renderer, String name) {
        this.renderer = renderer;
	    this.name = name;
    }

    @Override
    public IWorldDynamicObject<GLRenderer> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return new Block(renderer, name, center, width, height);
    }

}
