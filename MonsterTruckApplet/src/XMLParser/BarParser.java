package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldStaticObject;
import WorldObjects.Bar;

public class BarParser extends BarParserBase<Graphics> {

	@Override
	public IWorldStaticObject<Graphics> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Bar(A, B);
	}

}
