package com.studiocinqo.monstertruck.XMLParser;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.WorldObjects.Foreground;

import Auxiliary.IWorldNoninteractingObject;
import XMLParser.ForegoundParserBase;

public class ForegroundParser extends ForegoundParserBase<GLRenderer> {

    private final GLRenderer renderer;
    private final String name;

    public ForegroundParser(GLRenderer renderer, String name) {
	this.renderer = renderer;
	this.name = name;
    }

    @Override
    public IWorldNoninteractingObject<GLRenderer> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return new Foreground(renderer, name, pointA, pointB);
    }

}
