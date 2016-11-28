package XMLParser.TextureMap;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap.ScoreBoard;
import XMLParser.RectParser;

public class ScoreBoardParser implements IObjectParser<TextureMap.TextureMap.ScoreBoard>{

    public ScoreBoardParser() {
	// TODO Auto-generated constructor stub
    }
    
    private IObjectParser objectParser = null;
    private boolean sealed = false;
    private Rect text;
    private Rect digits;
    

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "text":
	    objectParser = new RectParser();
	case "digits":
	    objectParser = new RectParser();
	default:
	    throw new RuntimeException("Element not supported");
	}
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	if (objectParser != null)
	    objectParser.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
	switch (qName.toLowerCase()) {
	case "text":
	    objectParser.sealElement();
	    text = (Rect)objectParser.getObject();
	    objectParser = null;
	    break;
	case "digits":
	    objectParser.sealElement();
	    digits = (Rect)objectParser.getObject();
	    objectParser = null;
	default:
	    throw new RuntimeException("Element not supported");
	}
    }

    @Override
    public void sealElement() {
	sealed = true;
    }

    @Override
    public ScoreBoard getObject() {
	return new ScoreBoard(text, digits);
    }
    

}
