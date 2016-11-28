package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldDynamicObject;
import WorldObjects.Pendulum;

public class PendulumParser extends PendulumParserBase<Graphics> {

	@Override
	public IWorldDynamicObject<Graphics> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Pendulum(center, radius);
	}
	


}
