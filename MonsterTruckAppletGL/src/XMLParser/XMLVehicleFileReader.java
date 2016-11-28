package XMLParser;

import OpenGL.GLRenderer;
import VehicleObjects.Vehicle;
import VehicleObjects.VehicleBase;
import org.xml.sax.SAXException;

import java.io.IOException;

public class XMLVehicleFileReader extends XMLVehicleFileReaderBase<GLRenderer> {

    //private final TextureAtlas textureAtlas;

    public XMLVehicleFileReader() {
	//this.textureAtlas = textureAtlas;
	try {
	    //xmlReader.parse(convertToFileURL("resources/vehicle.xml"));
		xmlReader.parse(this.getClass().getResource("../resources/vehicle.xml").toString());
	} catch (IOException | SAXException e) {
	    throw new RuntimeException(e);
	}
    }

	@Override
	public VehicleBase<GLRenderer> getVehicle() {
		return new Vehicle(new VehicleBase.Geometry(geometricCentre, centerOfMass, CMtoBodyGeometry,
				positionFrontWheel,positionRearWheel, radiusWheel,
				frontBumper, rearBumper, mass, momentOfInertia));
	}


}
