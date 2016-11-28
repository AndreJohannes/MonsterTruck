package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldNoninteractingObject;
import Auxiliary.Vector2D;

public abstract class ForegoundParserBase<T> implements IObjectParser<IWorldNoninteractingObject<T>> {

	protected boolean sealed = false;
	private StringBuffer stringBuffer = null;

	protected Vector2D pointA = new Vector2D(0, 0);
	protected Vector2D pointB = new Vector2D(0, 0);

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
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
			pointA = new Vector2D(Double.parseDouble(stringBuffer.toString().split(",")[0]),
					Double.parseDouble(stringBuffer.toString().split(",")[1]));
			stringBuffer = null;
			break;
		case "b":
			pointB = new Vector2D(Double.parseDouble(stringBuffer.toString().split(",")[0]),
					Double.parseDouble(stringBuffer.toString().split(",")[1]));
			stringBuffer = null;
			break;
		}
	}

	@Override
	public void sealElement() {
		sealed = true;
		System.err.format("Foreground: (%f,%f) (%f,%f) %n", pointA.X, pointA.Y, pointB.X, pointB.Y);
	}

	@Override
	public abstract IWorldNoninteractingObject<T> getObject();

}
