package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldStaticObject;
import Auxiliary.Vector2D;

public abstract class ArcParserBase<T> implements IObjectParser<IWorldStaticObject<T>> {

    protected boolean sealed = false;
    private StringBuffer stringBuffer = null;

    protected Vector2D center = new Vector2D(0, 0);
    protected double radius = 30, startAngle = 180, arcAngle = 180;

    @Override
    public abstract IWorldStaticObject<T> getObject();

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "center":
	case "radius":
	case "startangle":
	case "arcangle":
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
	case "radius":
	    radius = Double.parseDouble(stringBuffer.toString());
	    stringBuffer = null;
	    break;
	case "startangle":
	    startAngle = Double.parseDouble(stringBuffer.toString());
	    stringBuffer = null;
	    break;
	case "arcangle":
	    arcAngle = Double.parseDouble(stringBuffer.toString());
	    stringBuffer = null;
	    break;
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
	System.err.format("Arc: %f %f %f %f %f  %n", center.X, center.Y, radius,
		startAngle, arcAngle);
    }

}
