package com.studiocinqo.monstertruck.XMLParser;

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import XMLParser.PointParser;
import XMLParser.XMLWorldFileReaderBase;
import android.content.res.AssetManager;
import android.graphics.Canvas;

public class XMLWorldFileReader extends XMLWorldFileReaderBase<GLRenderer> {

    private final GLRenderer renderer;

    public XMLWorldFileReader(AssetManager assets, GLRenderer renderer) {
	super();
	this.renderer = renderer;
	InputSource source;
	try {
	    source = new InputSource(assets.open("world.xml"));
	    xmlReader.parse(source);
	} catch (IOException | SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) throws SAXException {
	String name = atts.getValue("name");
	if (state == State.NONE && qName.toLowerCase() == "world") {
	    state = State.ROOT;
	} else if (state == State.ROOT) {
	    switch (qName.toLowerCase()) {
	    case "arc":
		state = State.STATIC_OBJECT;
		objectParser = new ArcParser();
		break;
	    case "point":
		state = State.STATIC_OBJECT;
		objectParser = new PointParser<Canvas>();
		break;
	    case "bar":
		state = State.STATIC_OBJECT;
		objectParser = new BarParser();
		break;
	    case "cylinder":
		state = State.DYNAMIC_OBJECT;
		objectParser = new CylinderParser(renderer, name);
		break;
	    case "bridge":
		state = State.DYNAMIC_OBJECT;
		objectParser = new BridgeParser(renderer, name);
		break;
	    case "block":
		state = State.DYNAMIC_OBJECT;
		objectParser = new BlockParser(renderer, name);
		break;
	    case "pendulum":
		state = State.DYNAMIC_OBJECT;
		objectParser = new PendulumParser(renderer, name);
		break;
	    case "doublependulum":
		state = State.DYNAMIC_OBJECT;
		objectParser = new DoublePendulumParser(renderer, name);
		break;
	    case "coin":
		state = State.TOKEN_OBJECT;
		objectParser = new CoinParser(renderer, name);
		break;
	    case "foreground":
		state = State.NONINTERACTING_OBJECT;
		objectParser = new ForegroundParser(renderer, name);
		break;
	    default:
		throw new RuntimeException(
			"This element is currently not supported");
	    }
	} else {
	    objectParser.startElement(namespaceURI, localName, qName, atts);
	}

    }

}
