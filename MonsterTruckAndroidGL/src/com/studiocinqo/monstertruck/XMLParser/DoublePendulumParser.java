package com.studiocinqo.monstertruck.XMLParser;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.WorldObjects.DoublePendulum;

import Auxiliary.IWorldDynamicObject;
import XMLParser.DoublePendulumParserBase;

public class DoublePendulumParser extends DoublePendulumParserBase<GLRenderer>{

	private final GLRenderer renderer;
	private final String name;    
	
    	public DoublePendulumParser(GLRenderer renderer, String name) {
    	    this.renderer = renderer;
    	    this.name = name;
    	}
    
	@Override
	public IWorldDynamicObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new DoublePendulum(renderer, name, center);
	}


}
