package XMLParser;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;

public class DoubleParser implements IObjectParser<Double> {

    private double value;

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	throw new RuntimeException(
		"The Double XML parser does not allow sub-elements");
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append(ch, start, length);
	value = Double.parseDouble(stringBuffer.toString().split(",")[0]);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
	throw new RuntimeException(
		"The Double XML parser does not allow sub-elements");

    }

    @Override
    public void sealElement() {
    }

    @Override
    public Double getObject() {
	return value;
    }

}
