package com.studiocinqo.monstertruck.XMLParser;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.WorldObjects.Bridge;

import Auxiliary.IWorldDynamicObject;
import XMLParser.BridgeParserBase;

public class BridgeParser extends BridgeParserBase<GLRenderer> {

    private final GLRenderer renderer;
    private final String name;

    public BridgeParser(GLRenderer renderer, String name) {
	this.renderer = renderer;
	this.name = name;
    }

    @Override
    public IWorldDynamicObject<GLRenderer> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return new Bridge(renderer, name, startPoint, endPoint, numberOfLinks,
		suspension);
    }

}
