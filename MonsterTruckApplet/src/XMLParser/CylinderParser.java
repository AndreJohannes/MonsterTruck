package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldDynamicObject;
import WorldObjects.Cylinder;

public class CylinderParser extends CylinderParserBase<Graphics> {

	@Override
	public IWorldDynamicObject<Graphics> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Cylinder(center, radius);
	}


}
