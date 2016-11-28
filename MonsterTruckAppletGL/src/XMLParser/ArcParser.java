package XMLParser;

import Auxiliary.IWorldStaticObject;
import OpenGL.GLRenderer;
import WorldObjects.Arc;

public class ArcParser extends ArcParserBase<GLRenderer> {

	@Override
	public IWorldStaticObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Arc(center, radius, startAngle, arcAngle);
	}

}
