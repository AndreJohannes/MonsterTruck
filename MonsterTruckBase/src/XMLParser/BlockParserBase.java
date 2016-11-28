package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.Vector2D;

public abstract class BlockParserBase<T> implements IObjectParser<IWorldDynamicObject<T>> {

    protected boolean sealed = false;
    private StringBuffer stringBuffer = null;

    protected Vector2D center = new Vector2D(0, 0);
    protected double height = 50, width = 50;

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "center":
	case "height":
	case "width":
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
	case "center":
	    center = new Vector2D(
		    Double.parseDouble(stringBuffer.toString().split(",")[0]),
		    Double.parseDouble(stringBuffer.toString().split(",")[1]));
	    stringBuffer = null;
	    break;
	case "height":
	    height = Double.parseDouble(stringBuffer.toString());
	    stringBuffer = null;
	    break;
	case "width":
	    width = Double.parseDouble(stringBuffer.toString());
	    stringBuffer = null;
	    break;
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
	System.err.format("Block: [%f %f], %f%f  %n", center.X, center.Y,
		height, width);
    }

    @Override
    public abstract IWorldDynamicObject<T> getObject();

}
