package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.IWorldTokenObject;
import Auxiliary.Vector2D;

public abstract class CoinParserBase<T> implements IObjectParser<IWorldTokenObject<T>> {

	protected boolean sealed = false;
	private StringBuffer stringBuffer = null;

	protected Vector2D center = new Vector2D(0, 0);

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
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
			center = new Vector2D(Double.parseDouble(stringBuffer.toString().split(",")[0]),
					Double.parseDouble(stringBuffer.toString().split(",")[1]));
			stringBuffer = null;
			break;
		}
	}

	@Override
	public void sealElement() {
		sealed = true;
		System.err.format("Coin: %f %f  %n", center.X, center.Y);
	}

	@Override
	public abstract IWorldTokenObject<T> getObject();

}
