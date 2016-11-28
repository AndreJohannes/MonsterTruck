package XMLParser;

import Auxiliary.IWorldTokenObject;
import OpenGL.GLRenderer;
import WorldObjects.Coin;

public class CoinParser extends CoinParserBase<GLRenderer> {

	private final GLRenderer renderer;
	private final String name;

	public CoinParser(GLRenderer renderer, String name) {
		this.renderer = renderer;
		this.name = name;
	}

	@Override
	public IWorldTokenObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Coin(renderer, name, center);
	}


}

