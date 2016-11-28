package XMLParser;

import java.awt.Graphics;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import Graphics.TextureAtlas;

public class XMLWorldFileReader extends XMLWorldFileReaderBase<Graphics> {

    private final TextureAtlas textureAtlas;

    public XMLWorldFileReader(TextureAtlas textureAtlas) {
	super();
	this.textureAtlas = textureAtlas;
	try {
	    xmlReader.parse(convertToFileURL("resources/world.xml"));
	} catch (IOException | SAXException e) {
	    throw new RuntimeException(e);
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
		objectParser = new PointParser<Graphics>();
		break;
	    case "bar":
		state = State.STATIC_OBJECT;
		objectParser = new BarParser();
		break;
	    case "cylinder":
		state = State.DYNAMIC_OBJECT;
		objectParser = new CylinderParser();
		break;
	    case "bridge":
		state = State.DYNAMIC_OBJECT;
		objectParser = new BridgeParser();
		break;
	    case "block":
		state = State.DYNAMIC_OBJECT;
		objectParser = new BlockParser(textureAtlas, name);
		break;
	    case "pendulum":
		state = State.DYNAMIC_OBJECT;
		objectParser = new PendulumParser();
		break;
	    case "doublependulum":
		state = State.DYNAMIC_OBJECT;
		objectParser = new DoublePendulumParser();
		break;
	    case "coin":
		state = State.TOKEN_OBJECT;
		objectParser = new CoinParser();
		break;
	    case "foreground":
		state = State.NONINTERACTING_OBJECT;
		objectParser = new ForegroundParser(textureAtlas, name);
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
