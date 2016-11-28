package com.studiocinqo.monstertruck.XMLParser;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.WorldObjects.Bar;

import Auxiliary.IWorldStaticObject;
import XMLParser.BarParserBase;

public class BarParser extends BarParserBase<GLRenderer> {

	public IWorldStaticObject<GLRenderer> getObject() {
		if (!sealed)
			throw new RuntimeException("XML element has not been sealed");
		return new Bar(A, B);
	}

}
