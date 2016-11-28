	package com.studiocinqo.monstertruck.XMLParser;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.WorldObjects.Arc;

import Auxiliary.IWorldStaticObject;
import XMLParser.ArcParserBase;

public class ArcParser extends ArcParserBase<GLRenderer> {

	@Override
	public IWorldStaticObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Arc(center, radius, startAngle, arcAngle);
	}


}
