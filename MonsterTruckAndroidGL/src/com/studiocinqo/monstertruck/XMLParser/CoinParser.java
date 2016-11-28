package com.studiocinqo.monstertruck.XMLParser;


import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.WorldObjects.Coin;

import Auxiliary.IWorldTokenObject;
import XMLParser.CoinParserBase;

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
