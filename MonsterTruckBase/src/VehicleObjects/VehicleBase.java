package VehicleObjects;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.IVehicleObject;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.IWorldStaticObject;
import Auxiliary.IWorldTokenObject;
import Auxiliary.Vector2D;

public abstract class VehicleBase<T> {

    @XmlRootElement
    public static class Geometry {

	public final Vector2D geometricCentre;
	public final Vector2D centerOfMass;
	public final Vector2D positionFrontWheel;
	public final Vector2D positionRearWheel;
	public final Vector2D CMtoFrontWheel;
	public final Vector2D CMtoRearWheel;
	public final List<Vector2D> CMtoBodyGeometry;
	public final double radiusWheel;
	public final Vector2D.Line CMtoFrontBumper;
	public final Vector2D.Line CMtoRearBumper;
	public final double mass;
	public final double momentOfInertia;

	public Geometry(Vector2D geometricCentre, Vector2D centreOfMass,
		List<Vector2D> bodyGeometry, Vector2D positionFrontWheel,
		Vector2D positionRearWheel, double radius,
		Vector2D.Line frontBumper, Vector2D.Line rearBumper,
		double mass, double momentOfInertia) {
	    this.geometricCentre = geometricCentre;
	    this.centerOfMass = centreOfMass;
	    this.positionFrontWheel = positionFrontWheel;
	    this.positionRearWheel = positionRearWheel;
	    this.CMtoFrontWheel = positionFrontWheel.subs(centreOfMass);
	    this.CMtoRearWheel = positionRearWheel.subs(centerOfMass);
	    this.radiusWheel = radius;
	    this.CMtoFrontBumper = new Vector2D.Line(
		    frontBumper.A.subs(centreOfMass),
		    frontBumper.B.subs(centreOfMass));
	    this.CMtoRearBumper = new Vector2D.Line(
		    rearBumper.A.subs(centreOfMass),
		    rearBumper.B.subs(centreOfMass));
	    this.mass = mass;
	    this.momentOfInertia = momentOfInertia;
	    if (bodyGeometry != null && !bodyGeometry.isEmpty()) {
		this.CMtoBodyGeometry = new LinkedList<Vector2D>();
		for (Vector2D vector : bodyGeometry) {
		    this.CMtoBodyGeometry.add(vector.subs(centerOfMass));
		}
	    } else
		this.CMtoBodyGeometry = null;
	}
    }

    protected final Geometry geometry;
    // private final GameEngineFactoryBase<T> factory;
    private final Bumper<T> frontBumper;
    private final Bumper<T> rearBumper;
    private final Wheel<T> frontWheel;
    private final Wheel<T> rearWheel;
    private final Carbody<T> bodywork;
    private double throttle = 0;
    private double health = 100;

    public VehicleBase(Geometry geometry) {
	// this.factory = factory;
	this.geometry = geometry;
	this.frontBumper = new Bumper.FrontBumper<T>(geometry, this);
	this.rearBumper = new Bumper.RearBumper<T>(geometry, this);
	this.frontWheel = new Wheel.FrontWheel<T>(geometry);
	this.rearWheel = new Wheel.RearWheel<T>(geometry);
	this.bodywork = new Carbody<T>(geometry, this);
    }

    public List<IWorldTokenObject<T>> collectTokens(double[] state) {
	double positionX = state[0], positionY = state[1];
	double rad = state[4];
	double sin = Math.sin(rad), cos = Math.cos(rad);
	Vector2D point = geometry.CMtoBodyGeometry.get(0);
	Vector2D location = new Vector2D(
		positionX + cos * point.X + sin * point.Y,
		positionY + cos * point.Y - sin * point.X);
	return new LinkedList();// factory.getTokens(location, 50);
    }

    public abstract void draw(T gr, double positionCM_x, double positionCM_y,
	    double angle, double angleFrontWheel, double angleRearWheel,
	    double displacementFrontWheelX, double displacementFrontWheelY,
	    double displacementRearWheelX, double displacementRearWheelY);

    public double[] function(double[] state, double[] outstate,
	    List<CollisionMatrix> objectsFrontWheel,
	    List<CollisionMatrix> objectsRearWheel) {
	double positionX = state[0], positionY = state[1];
	double velocityX = state[2], velocityY = state[3];
	double rad = state[4], angularVelocity = state[5];
	double sin = Math.sin(rad), cos = Math.cos(rad);
	// Next we calculate the position of the center of the front and
	// rear
	// wheel

	double forceFrontWheelX = 0, forceFrontWheelY = 0;
	Vector2D displacementFrontWheel = Vector2D.getZeroVector();
	double velocityFrontWheel = 3 * (velocityX + velocityY) / 1.4;
	boolean touchFrontWheel = false;
	for (CollisionMatrix object : objectsFrontWheel) {
	    double extension = object.distance;
	    Vector2D normal = object.normal;
	    touchFrontWheel = true;
	    Vector2D force = new Vector2D(extension * normal.X,
		    extension * normal.Y);
	    forceFrontWheelX += force.X - throttle * normal.Y;
	    forceFrontWheelY += force.Y + throttle * normal.X;
	    if (object.collisionObject2.callbackFunction != null)
		object.collisionObject2.callbackFunction
			.callbackFunctionForce(force.scale(-5d));
	    displacementFrontWheel = updateDisplacments(displacementFrontWheel,
		    normal, object.distance);
	    velocityFrontWheel = 5
		    * (-normal.Y * velocityX + normal.X * velocityY);
	}

	double forceRearWheelX = 0, forceRearWheelY = 0;
	Vector2D displacementRearWheel = Vector2D.getZeroVector();
	double velocityRearWheel = 3 * (velocityX + velocityY) / 1.4;
	boolean touchRearWheel = false;
	for (CollisionMatrix object : objectsRearWheel) {
	    double extension = object.distance;
	    Vector2D normal = object.normal;
	    touchRearWheel = true;
	    Vector2D force = new Vector2D(extension * normal.X,
		    extension * normal.Y);
	    forceRearWheelX += force.X - throttle * normal.Y;
	    forceRearWheelY += force.Y + throttle * normal.X;
	    if (object.collisionObject2.callbackFunction != null)
		object.collisionObject2.callbackFunction
			.callbackFunctionForce(force.scale(-1d));
	    displacementRearWheel = updateDisplacments(displacementRearWheel,
		    normal, object.distance);
	    velocityRearWheel = 5
		    * (-normal.Y * velocityX + normal.X * velocityY);
	}

	double gForce = 10;
	double friction = 0.1 + (touchFrontWheel ? 0.3 : 0.0)
		+ (touchRearWheel ? 0.3 : 0.0);
	double forceX = 2 * forceFrontWheelX + 2 * forceRearWheelX
		- friction * velocityX;
	double forceY = 2 * forceFrontWheelY + 2 * forceRearWheelY
		- friction * velocityY + gForce;
	double torque = forceFrontWheelX
		* (cos * geometry.CMtoFrontWheel.Y
			- sin * geometry.CMtoFrontWheel.X)
		+ forceFrontWheelY * (-cos * geometry.CMtoFrontWheel.X
			- sin * geometry.CMtoFrontWheel.Y)
		+ forceRearWheelX * (cos * geometry.CMtoRearWheel.Y
			- sin * geometry.CMtoRearWheel.X)
		+ forceRearWheelY * (-cos * geometry.CMtoRearWheel.X
			- sin * geometry.CMtoRearWheel.Y);

	outstate[0] = velocityX;
	outstate[1] = velocityY;
	outstate[2] = forceX;
	outstate[3] = forceY;
	outstate[4] = angularVelocity;
	outstate[5] = 0.0003 * torque - 0.08 * angularVelocity;
	outstate[6] = velocityFrontWheel;
	outstate[7] = velocityRearWheel;

	double[] returnData = new double[] { displacementFrontWheel.X,
		displacementFrontWheel.Y, displacementRearWheel.X,
		displacementRearWheel.Y };

	return returnData;
    }

    public Geometry getGeometry() {
	return geometry;
    }

    public double getHealth() {
	return health;
    }

    public List<IVehicleObject<T>> getVehicleParts() {
	List<IVehicleObject<T>> retList = new LinkedList<IVehicleObject<T>>();
	retList.add(frontBumper);
	retList.add(rearBumper);
	retList.add(bodywork);
	retList.add(frontWheel);
	retList.add(rearWheel);
	return retList;
    }

    public void incHealth(double value) {
	this.health += value;
    }

    public void setHealth(double health) {
	this.health = health;
    }

    public void setThrottle(double throttle) {
	this.throttle = throttle;
    }

    private Vector2D updateDisplacments(Vector2D displacements, Vector2D normal,
	    double extension) {
	double currentExtension = displacements.X * normal.X
		+ displacements.Y * normal.Y;
	if ((extension > 0) && (extension > currentExtension))
	    return new Vector2D(
		    displacements.X + normal.X * (extension - currentExtension),
		    displacements.Y
			    + normal.Y * (extension - currentExtension));
	if ((extension < 0) && (extension < currentExtension))
	    return new Vector2D(
		    displacements.X + normal.X * (extension - currentExtension),
		    displacements.Y
			    + normal.Y * (extension - currentExtension));
	return displacements;
    }

}
