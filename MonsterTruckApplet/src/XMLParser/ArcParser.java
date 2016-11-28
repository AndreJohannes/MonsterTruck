package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldStaticObject;
import WorldObjects.Arc;

public class ArcParser extends ArcParserBase<Graphics> {

	@Override
	public IWorldStaticObject<Graphics> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Arc(center, radius, startAngle, arcAngle);
	}

}
