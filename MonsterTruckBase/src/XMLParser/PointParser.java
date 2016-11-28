package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldStaticObject;
import Auxiliary.Vector2D;
import WorldObjects.Point;

public class PointParser<T> implements IObjectParser<IWorldStaticObject<T>> {

    private boolean sealed = false;
    private StringBuffer stringBuffer = null;

    private Vector2D center = new Vector2D(0, 0);

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "center":
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
	if (sealed)
	    return;
	switch (qName.toLowerCase()) {
	case "center":
	    center = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
	System.err.format("Point: %f %f %n", center.X, center.Y);
    }

    @Override
    public IWorldStaticObject<T> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return new Point<T>(center);
    }

}
