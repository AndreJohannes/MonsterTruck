package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.Vector2D;
import WorldObjects.BridgeBase;

public abstract class BridgeParserBase<T> implements IObjectParser<IWorldDynamicObject<T>> {

    protected boolean sealed = false;
    private StringBuffer stringBuffer = null;

    protected Vector2D startPoint = new Vector2D(0, 300);
    protected Vector2D endPoint = new Vector2D(200, 300);
    protected BridgeBase.Suspension suspension = new BridgeBase.Suspension(50);
    protected int numberOfLinks = 10;

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "startpoint":
	case "endpoint":
	case "suspension":
	case "links":
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
	case "startpoint":
	    startPoint = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	case "endpoint":
	    endPoint = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	case "suspension":
	    suspension = new BridgeBase.Suspension(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]),
		    Double.parseDouble(stringBuffer.toString().split(",")[2]));
	    stringBuffer = null;
	    break;
	case "links":
	    numberOfLinks = Integer.parseInt(stringBuffer.toString());
	    break;
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
	System.err.format("Bridge: [%f %f], [%f %f], %d   %n", startPoint.X,
		startPoint.Y, endPoint.X, endPoint.Y, numberOfLinks);
    }

    @Override
    public abstract IWorldDynamicObject<T> getObject();

}
