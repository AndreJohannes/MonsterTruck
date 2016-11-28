package XMLParser;

import Auxiliary.IWorldDynamicObject;
import OpenGL.GLRenderer;
import WorldObjects.Pendulum;

public class PendulumParser extends PendulumParserBase<GLRenderer> {

    private final GLRenderer renderer;
	private final String name;

	public PendulumParser(GLRenderer renderer, String name) {
	    this.renderer = renderer;
	    this.name= name;
	}

	@Override
	public IWorldDynamicObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Pendulum(renderer,name, center, radius);
	}
	

}
