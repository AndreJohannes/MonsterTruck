package XMLParser;

import java.io.File;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D;
import VehicleObjects.VehicleBase;

public abstract class XMLVehicleFileReaderBase<T> extends DefaultHandler {

    private State state = State.NONE;
    @SuppressWarnings("rawtypes")
    private IObjectParser objectParser = null;
    protected XMLReader xmlReader;

    protected Vector2D geometricCentre;
    protected Vector2D centerOfMass;
    protected Vector2D positionFrontWheel;
    protected Vector2D positionRearWheel;
    protected List<Vector2D> CMtoBodyGeometry;
    protected double radiusWheel;
    protected Vector2D.Line frontBumper;
    protected Vector2D.Line rearBumper;
    public double mass;
    public double momentOfInertia;

    private enum State {
	NONE, ROOT, ELEMENT;
    }

    abstract public VehicleBase<T> getVehicle();

    public XMLVehicleFileReaderBase() {
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
	if (state == State.NONE && qName.toLowerCase() == "vehicle") {
	    state = State.ROOT;
	} else if (state == State.ROOT) {
	    switch (qName.toLowerCase()) {
	    case "geometriccenter":
	    case "centerofmass":
	    case "positionfrontwheel":
	    case "positionrearwheel":
		state = State.ELEMENT;
		objectParser = new Vector2DParser();
		break;
	    case "bodygeometry":
		state = State.ELEMENT;
		objectParser = new ListParser<Vector2D>(new Vector2DParser());
		break;
	    case "radiuswheel":
	    case "mass":
	    case "momentofinertia":
		state = State.ELEMENT;
		objectParser = new DoubleParser();
		break;
	    case "frontbumper":
	    case "rearbumper":
		state = State.ELEMENT;
		objectParser = new LineParser();
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
	    case "geometriccenter":
		objectParser.sealElement();
		geometricCentre = (Vector2D) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "centerofmass":
		objectParser.sealElement();
		centerOfMass = (Vector2D) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "positionfrontwheel":
		objectParser.sealElement();
		positionFrontWheel = (Vector2D) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "positionrearwheel":
		objectParser.sealElement();
		positionRearWheel = (Vector2D) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "bodygeometry":
		objectParser.sealElement();
		CMtoBodyGeometry = (List<Vector2D>) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "radiuswheel":
		objectParser.sealElement();
		radiusWheel = (Double) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "mass":
		objectParser.sealElement();
		mass = (Double) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "momentofinertia":
		objectParser.sealElement();
		momentOfInertia = (Double) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "frontbumper":
		objectParser.sealElement();
		frontBumper = (Vector2D.Line) objectParser.getObject();
		objectParser = null;
		state = State.ROOT;
		break;
	    case "rearbumper":
		objectParser.sealElement();
		rearBumper = (Vector2D.Line) objectParser.getObject();
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
