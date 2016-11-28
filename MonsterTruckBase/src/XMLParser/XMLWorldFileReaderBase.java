package XMLParser;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.IWorldNoninteractingObject;
import Auxiliary.IWorldStaticObject;
import Auxiliary.IWorldTokenObject;

public abstract class XMLWorldFileReaderBase<T> extends DefaultHandler {

    protected State state = State.NONE;
    @SuppressWarnings("rawtypes")
    protected IObjectParser objectParser = null;
    protected XMLReader xmlReader;
    
    private List<IWorldStaticObject<T>> staticObjectList = new LinkedList<IWorldStaticObject<T>>();
    private List<IWorldDynamicObject<T>> dynamicObjectList = new LinkedList<IWorldDynamicObject<T>>();
    private List<IWorldTokenObject<T>> tokenObjectList = new LinkedList<IWorldTokenObject<T>>();
    private List<IWorldNoninteractingObject<T>> noninteractingObjectList = new LinkedList<IWorldNoninteractingObject<T>>();

    protected enum State {
	NONE, ROOT, STATIC_OBJECT, DYNAMIC_OBJECT, TOKEN_OBJECT, NONINTERACTING_OBJECT;
    }

    public XMLWorldFileReaderBase() {
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

    public List<IWorldStaticObject<T>> getStaticObjectList() {
	return staticObjectList;
    }

    public List<IWorldDynamicObject<T>> getDynamicObjectList() {
	return dynamicObjectList;
    }

    public List<IWorldTokenObject<T>> getTokenObjectList() {
	return tokenObjectList;
    }

    public List<IWorldNoninteractingObject<T>> getNoninteractingObjectList() {
	return noninteractingObjectList;
    }

    @Override
    public abstract void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) throws SAXException;

    @Override
    public void characters(char ch[], int start, int length) {
	if (objectParser != null)
	    objectParser.characters(ch, start, length);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if (state == State.STATIC_OBJECT) {
	    switch (qName.toLowerCase()) {
	    case "point":
	    case "arc":
	    case "bar":
		objectParser.sealElement();
		staticObjectList
			.add((IWorldStaticObject<T>) objectParser.getObject());
		state = State.ROOT;
		break;
	    default:
		objectParser.endElement(uri, localName, qName);
	    }
	} else if (state == State.DYNAMIC_OBJECT) {
	    switch (qName.toLowerCase()) {
	    case "cylinder":
	    case "bridge":
	    case "block":
	    case "pendulum":
	    case "doublependulum":
		objectParser.sealElement();
		dynamicObjectList
			.add((IWorldDynamicObject<T>) objectParser.getObject());
		state = State.ROOT;
		break;
	    default:
		objectParser.endElement(uri, localName, qName);
	    }
	} else if (state == State.TOKEN_OBJECT) {
	    switch (qName.toLowerCase()) {
	    case "coin":
		objectParser.sealElement();
		tokenObjectList
			.add((IWorldTokenObject<T>) objectParser.getObject());
		state = State.ROOT;
		break;
	    default:
		objectParser.endElement(uri, localName, qName);
	    }
	} else if (state == State.NONINTERACTING_OBJECT) {
	    switch (qName.toLowerCase()) {
	    case "foreground":
		objectParser.sealElement();
		noninteractingObjectList.add(
			(IWorldNoninteractingObject<T>) objectParser.getObject());
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
