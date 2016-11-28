package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldStaticObject;
import Auxiliary.Vector2D;

public abstract class BarParserBase<T> implements IObjectParser<IWorldStaticObject<T>> {

    protected Vector2D A = new Vector2D(0, 0);
    protected Vector2D B = new Vector2D(500, 500);

    private StringBuffer stringBuffer = null;
    protected boolean sealed = false;

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
	if (sealed)
	    return;
	switch (qName.toLowerCase()) {
	case "a":
	    A = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	case "b":
	    B = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
	System.err.format("Bar: %f %f %f %f  %n", A.X, A.Y, B.X, B.Y);
    }

    @Override
    public abstract IWorldStaticObject<T> getObject();

}
