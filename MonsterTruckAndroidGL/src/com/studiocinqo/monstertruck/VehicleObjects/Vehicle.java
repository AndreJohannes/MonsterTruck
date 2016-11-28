package com.studiocinqo.monstertruck.VehicleObjects;

import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import VehicleObjects.VehicleBase;
import android.content.Context;

public class Vehicle extends VehicleBase<GLRenderer> {

    public Vehicle(Context context, Geometry geometry) {
	super(geometry);
    }

    public void draw(GLRenderer renderer, double positionCM_x,
	    double positionCM_y, double angle, double angleFrontWheel,
	    double angleRearWheel, double displacementFrontWheelX,
	    double displacementFrontWheelY, double displacementRearWheelX,
	    double displacementRearWheelY) {
	// TODO : Race competition????
	double sin = Math.sin(Math.toRadians(-angle));
	double cos = Math.cos(Math.toRadians(-angle));
	renderer.getVehicle().setPositionAndOrientation(
		positionCM_x - cos * geometry.centerOfMass.X
			- sin * geometry.centerOfMass.Y,
		720 - positionCM_y + cos * geometry.centerOfMass.Y
			- sin * geometry.centerOfMass.X,
		Math.toRadians(angle));
	renderer.getFrontWheel().setPositionAndOrientation(
		positionCM_x + cos * geometry.CMtoFrontWheel.X
			+ sin * geometry.CMtoFrontWheel.Y
			+ displacementFrontWheelX,
		720 - positionCM_y - cos * geometry.CMtoFrontWheel.Y
			+ sin * geometry.CMtoFrontWheel.X
			- displacementFrontWheelY,
		Math.toRadians(-angleFrontWheel + angle));
	renderer.getRearWheel().setPositionAndOrientation(
		positionCM_x + cos * geometry.CMtoRearWheel.X
			+ sin * geometry.CMtoRearWheel.Y
			+ displacementRearWheelX,
		720 - positionCM_y - cos * geometry.CMtoRearWheel.Y
			+ sin * geometry.CMtoRearWheel.X
			- displacementRearWheelY,
		Math.toRadians(-angleRearWheel + angle));
    }

}
