package XMLParser;

import Auxiliary.IWorldStaticObject;
import OpenGL.GLRenderer;
import WorldObjects.Bar;

public class BarParser extends BarParserBase<GLRenderer> {

	@Override
	public IWorldStaticObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Bar(A, B);
	}

}
