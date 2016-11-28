package XMLParser;

import java.awt.Graphics;

import Auxiliary.IWorldTokenObject;
import WorldObjects.Coin;

public class CoinParser extends CoinParserBase<Graphics> {

	@Override
	public IWorldTokenObject<Graphics> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Coin(center);
	}


}
