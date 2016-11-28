package Auxiliary;

import org.xml.sax.Attributes;

public interface IObjectParser<T> {

    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts);

    public void characters(char ch[], int start, int length);

    public void endElement(String uri, String localName, String qName);

    public void sealElement();

    public T getObject();

}
