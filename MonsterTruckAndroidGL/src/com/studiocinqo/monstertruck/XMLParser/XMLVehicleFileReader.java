package com.studiocinqo.monstertruck.XMLParser;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;
import com.studiocinqo.monstertruck.VehicleObjects.Vehicle;

import VehicleObjects.VehicleBase;
import XMLParser.XMLVehicleFileReaderBase;
import android.content.Context;

public class XMLVehicleFileReader extends XMLVehicleFileReaderBase<GLRenderer> {

	private final Context context;
	
	public XMLVehicleFileReader(Context context) {
		super();
		this.context = context;
		InputSource source;
		try {
			source = new InputSource(context.getAssets().open("vehicle.xml"));
			xmlReader.parse(source);
		} catch (IOException | SAXException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public VehicleBase<GLRenderer> getVehicle() {
		return new Vehicle(context, new VehicleBase.Geometry(geometricCentre, centerOfMass, CMtoBodyGeometry, positionFrontWheel,
				positionRearWheel, radiusWheel, frontBumper, rearBumper, mass, momentOfInertia));
	}

}
