package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D;

public class RectParser implements IObjectParser<Vector2D.Rect> {

    private Vector2D.Rect rect = new Vector2D.Rect(0f, 0f, 0f, 0f);
    private final String name;

    public RectParser() {
	this.name = null;
    }

    public RectParser(String name) {
	this.name = name;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	throw new RuntimeException(
		"The Rect XML parser does not allow sub-elements");
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append(ch, start, length);
	rect = new Vector2D.Rect(
		Float.parseFloat(stringBuffer.toString().split(",")[0]),
		Float.parseFloat(stringBuffer.toString().split(",")[1]),
		Float.parseFloat(stringBuffer.toString().split(",")[2]),
		Float.parseFloat(stringBuffer.toString().split(",")[3]));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
	throw new RuntimeException(
		"The Vector2D XML parser does not allow sub-elements");
    }

    @Override
    public void sealElement() {
	//
    }

    @Override
    public Vector2D.Rect getObject() {
	return rect;
    }

    public String getName() {
	return name;
    }

}
