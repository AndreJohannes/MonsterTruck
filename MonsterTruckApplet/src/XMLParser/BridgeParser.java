package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldDynamicObject;
import WorldObjects.Bridge;

public class BridgeParser extends BridgeParserBase<Graphics> {

	@Override
	public IWorldDynamicObject<Graphics> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Bridge(startPoint, endPoint, numberOfLinks, suspension);
	}


}
