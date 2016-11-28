package XMLParser;


import Auxiliary.IWorldDynamicObject;
import OpenGL.GLRenderer;
import WorldObjects.Cylinder;

public class CylinderParser extends CylinderParserBase<GLRenderer> {

	private final GLRenderer renderer;
	private final String name;

	public CylinderParser(GLRenderer renderer, String name) {
		this.renderer = renderer;
		this.name = name;
	}

	@Override
	public IWorldDynamicObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Cylinder(renderer, name, center, radius);
	}


}
