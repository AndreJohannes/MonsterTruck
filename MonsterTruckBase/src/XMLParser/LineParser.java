package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D;

public class LineParser implements IObjectParser<Vector2D.Line> {

    private Vector2D pointA = new Vector2D(0, 0);
    private Vector2D pointB = new Vector2D(0, 0);

    private boolean sealed = false;
    private StringBuffer stringBuffer = null;

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "a":
	case "b":
	    stringBuffer = new StringBuffer();
	}
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	if (stringBuffer == null || sealed)
	    return;
	stringBuffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
	switch (qName.toLowerCase()) {
	case "a":
	    pointA = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	case "b":
	    pointB = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	}
    }

    @Override
    public void sealElement() {
	//
	sealed = true;
    }

    @Override
    public Vector2D.Line getObject() {
	return new Vector2D.Line(pointA, pointB);
    }

}
