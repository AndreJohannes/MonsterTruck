package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldDynamicObject;
import WorldObjects.DoublePendulum;

public class DoublePendulumParser extends DoublePendulumParserBase<Graphics>{

	@Override
	public IWorldDynamicObject<Graphics> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new DoublePendulum(center);
	}


}
