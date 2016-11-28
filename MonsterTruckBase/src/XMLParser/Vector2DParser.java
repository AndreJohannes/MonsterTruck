package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D;

public class Vector2DParser implements IObjectParser<Vector2D> {

    private Vector2D point = new Vector2D(0, 0);

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	throw new RuntimeException(
		"The Vector2D XML parser does not allow sub-elements");
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append(ch, start, length);
	point = new Vector2D(
		Double.parseDouble(stringBuffer.toString().split(",")[0]),
		Double.parseDouble(stringBuffer.toString().split(",")[1]));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
	throw new RuntimeException(
		"The Vector2D XML parser does not allow sub-elements");

    }

    @Override
    public void sealElement() {
    }

    @Override
    public Vector2D getObject() {
	return point;
    }

}
