package XMLParser.TextureMap;

import org.xml.sax.Attributes;

import Auxiliary.IObjectParser;
import Auxiliary.Vector2D.Rect;
import TextureMap.TextureMap.Landscape;
import XMLParser.DoubleParser;
import XMLParser.RectParser;

public class LandscapeParser implements IObjectParser<TextureMap.TextureMap.Landscape>{

    private IObjectParser objectParser = null;
    private boolean sealed = false;
    private Rect area;
    private double scale;
    
    public LandscapeParser() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes atts) {
	switch (qName.toLowerCase()) {
	case "area":
	    objectParser = new RectParser();
	case "scale":
	    objectParser = new DoubleParser();
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
	case "area":
	    objectParser.sealElement();
	    area = (Rect)objectParser.getObject();
	    objectParser = null;
	    break;
	case "scale":
	    objectParser.sealElement();
	    scale = (Double)objectParser.getObject();
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
    public TextureMap.TextureMap.Landscape getObject() {
	return new Landscape(area, scale);
    }

}
