package XMLParser;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;

public class ListParser<T> implements IObjectParser<List<T>> {

    private boolean sealed = false;
    private IObjectParser<T> parser;
    private boolean readItem = false;
    private List<T> returnList = new LinkedList<T>();

    public ListParser(IObjectParser<T> parser) {
	this.parser = parser;
    }

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "item":
	    readItem = true;
	    break;
	default:
	    throw new RuntimeException("Only list items are allowed");
	}
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	if (readItem == true)
	    parser.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
	switch (qName.toLowerCase()) {
	case "item":
	    parser.sealElement();
	    readItem = false;
	    returnList.add((T) parser.getObject());
	    break;
	default:
	    throw new RuntimeException("Only list items are allowed");
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
    }

    @Override
    public List<T> getObject() {
	if (!sealed)
	    throw new RuntimeException("XML element has not been sealed");
	return returnList;
    }

}