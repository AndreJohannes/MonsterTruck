package XMLParser;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap;
import TextureMap.TextureMap.Landscape;
import TextureMap.TextureMap.ScoreBoard;
import TextureMap.TextureMap.Texture;
import TextureMap.TextureMap.Vehicle;
import XMLParser.TextureMap.LandscapeParser;
import XMLParser.TextureMap.ScoreBoardParser;
import XMLParser.TextureMap.VehicleParser;

public class XMLTextureFileReader<T> extends DefaultHandler {

    private State state = State.NONE;
    @SuppressWarnings("rawtypes")
    private IObjectParser objectParser = null;
    protected XMLReader xmlReader;

    protected Vector2D.Rect background;
    protected Vector2D.Rect healthBar;
    protected Landscape landscape;
    protected ScoreBoard scoreBoard;
    protected final HashMap<String, Rect> textureByName = new HashMap<String, Rect>();
    protected final HashMap<String, Vehicle> vehicleByName = new HashMap<String, Vehicle>();

    private enum State {
	NONE, ROOT, ELEMENT;
    }

    public TextureMap getTextureMap(){
	return new TextureMap();
    }

    public XMLTextureFileReader() {
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser parser;
	try {
	    parser = factory.newSAXParser();
	    xmlReader = parser.getXMLReader();
	    xmlReader.setContentHandler(this);
	} catch (ParserConfigurationException | SAXException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) throws SAXException {
	String name = atts.getValue("name");
	if (state == State.NONE && qName.toLowerCase() == "texturemap") {
	    state = State.ROOT;
	} else if (state == State.ROOT) {
	    switch (qName.toLowerCase()) {
	    case "background":
	    case "healthbar":
	    case "rect":
		state = State.ELEMENT;
		objectParser = new RectParser(name);
	    case "scoreboard":
		state = State.ELEMENT;
		objectParser = new ScoreBoardParser();
		break;
	    case "vehicle":
		state = State.ELEMENT;
		objectParser = new VehicleParser(name);
		break;
	    case "landcape":
		state = State.ELEMENT;
		objectParser = new LandscapeParser();
		break;
	    default:
		throw new RuntimeException(
			"This element is currently not supported");
	    }
	} else {
	    objectParser.startElement(namespaceURI, localName, qName, atts);
	}

    }

    @Override
    public void characters(char ch[], int start, int length) {
	if (objectParser != null)
	    objectParser.characters(ch, start, length);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if (state == State.ELEMENT) {
	    switch (qName.toLowerCase()) {
	    case "background":
		objectParser.sealElement();
		background = (Vector2D.Rect) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "healthbar":
		objectParser.sealElement();
		healthBar = (Vector2D.Rect) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "rect":
		objectParser.sealElement();
		textureByName.put(((RectParser) objectParser).getName(),
			(Vector2D.Rect) objectParser.getObject());
		objectParser = null;
		state = State.ROOT;
		break;
	    case "vehicle":
		objectParser.sealElement();
		vehicleByName.put(((VehicleParser) objectParser).getName(),
			(Vehicle) objectParser.getObject());
		objectParser = null;
		state = State.ROOT;
		break;
	    case "scoreboard":
		objectParser.sealElement();
		scoreBoard = (ScoreBoard) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "landscape":
		objectParser.sealElement();
		landscape = (Landscape) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    default:
		objectParser.endElement(uri, localName, qName);
	    }

	}

    };

    protected static String convertToFileURL(String filename) {
	String path = new File(filename).getAbsolutePath();
	if (File.separatorChar != '/') {
	    path = path.replace(File.separatorChar, '/');
	}

	if (!path.startsWith("/")) {
	    path = "/" + path;
	}
	return "file:" + path;
    }

}
