package XMLParser;


import Auxiliary.IWorldDynamicObject;
import OpenGL.GLRenderer;
import WorldObjects.DoublePendulum;

public class DoublePendulumParser extends DoublePendulumParserBase<GLRenderer>{

    private final GLRenderer renderer;
	private final String name;    
	
	public DoublePendulumParser(GLRenderer renderer, String name) {
	    this.renderer = renderer;
	    this.name = name;
	}

	@Override
	public IWorldDynamicObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new DoublePendulum(renderer, name, center);
	}


}
