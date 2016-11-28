package XMLParser;


import Auxiliary.IWorldNoninteractingObject;
import OpenGL.GLRenderer;
import WorldObjects.Foreground;

public class ForegroundParser extends ForegoundParserBase<GLRenderer> {

    private final GLRenderer renderer;
    private final String name;

    public ForegroundParser(GLRenderer renderer,String name) {
	this.renderer = renderer;
	this.name = name;
    }

    @Override
    public IWorldNoninteractingObject<GLRenderer> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return new Foreground(renderer, name, pointA, pointB);
    }

}
