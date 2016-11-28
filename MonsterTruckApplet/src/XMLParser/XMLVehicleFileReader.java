package XMLParser;

import java.awt.Graphics;
import java.io.IOException;

import org.xml.sax.SAXException;

import Graphics.TextureAtlas;
import VehicleObjects.Vehicle;
import VehicleObjects.VehicleBase;

public class XMLVehicleFileReader extends XMLVehicleFileReaderBase<Graphics> {

    private final TextureAtlas textureAtlas;

    public XMLVehicleFileReader(TextureAtlas textureAtlas) {
	this.textureAtlas = textureAtlas;
	try {
	    xmlReader.parse(convertToFileURL("resources/vehicle.xml"));
	} catch (IOException | SAXException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public VehicleBase<Graphics> getVehicle() {
	return new Vehicle(textureAtlas,
		new VehicleBase.Geometry(geometricCentre, centerOfMass,
			CMtoBodyGeometry, positionFrontWheel, positionRearWheel,
			radiusWheel, frontBumper, rearBumper, mass,
			momentOfInertia));
    }

}
