package XMLParser.TextureMap;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap.Vehicle;
import XMLParser.DoubleParser;
import XMLParser.RectParser;

public class VehicleParser implements IObjectParser<TextureMap.TextureMap.Vehicle> {

    public VehicleParser(String name) {
	this.name = name;
    }

    private final String name;
    private IObjectParser objectParser = null;
    private boolean sealed = false;
    private Rect bodywork;
    private Rect wheel;
    

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "bodywork":
	    objectParser = new RectParser();
	case "wheel":
	    objectParser = new RectParser();
	default:
	    throw new RuntimeException("Element not supported");
	}
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	if (objectParser != null)
	    objectParser.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
	switch (qName.toLowerCase()) {
	case "bodywork":
	    objectParser.sealElement();
	    bodywork = (Rect)objectParser.getObject();
	    objectParser = null;
	    break;
	case "wheel":
	    objectParser.sealElement();
	    wheel = (Rect)objectParser.getObject();
	    objectParser = null;
	default:
	    throw new RuntimeException("Element not supported");
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
    }
    
    public String getName(){
	return "";
    }

    @Override
    public Vehicle getObject() {
	return new Vehicle(bodywork, wheel);
    }

}
