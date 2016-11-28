package com.studiocinqo.monstertruck.XMLParser;


import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.WorldObjects.Pendulum;

import Auxiliary.IWorldDynamicObject;
import XMLParser.PendulumParserBase;

public class PendulumParser extends PendulumParserBase<GLRenderer> {

    	private final GLRenderer renderer;
    	private final String name;
    
    	public PendulumParser(GLRenderer renderer, String name) {
    	    this.renderer = renderer;
    	    this.name= name;
    	}
    
	@Override
	public IWorldDynamicObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Pendulum(renderer,name, center, radius);
	}
	


}
