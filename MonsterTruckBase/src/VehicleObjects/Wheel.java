package VehicleObjects;

import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.VehiclePartTypes;
import Auxiliary.IVehicleObject;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Circle;
import VehicleObjects.VehicleBase.Geometry;

public abstract class Wheel<T> implements IVehicleObject<T> {

    public static class FrontWheel<T> extends Wheel<T> {

	public FrontWheel(Geometry geometry) {
	    super(geometry.radiusWheel, geometry);
	}

	@Override
	public VehiclePartTypes getType() {
	    return VehiclePartTypes.FrontWheel;
	}

	@Override
	protected Vector2D getLocation(double[] state) {
	    double rad = state[4];
	    Vector2D CoM = new Vector2D(state[0], state[1]);
	    return geometry.CMtoFrontWheel.rotate(rad).add(CoM);
	}

    }

    public static class RearWheel<T> extends Wheel<T> {

	public RearWheel(Geometry geometry) {
	    super(geometry.radiusWheel, geometry);
	}

	@Override
	public VehiclePartTypes getType() {
	    return VehiclePartTypes.RearWheel;
	}

	@Override
	protected Vector2D getLocation(double[] state) {
	    double rad = state[4];
	    Vector2D CoM = new Vector2D(state[0], state[1]);
	    return geometry.CMtoRearWheel.rotate(rad).add(CoM);
	}

    }

    protected final double radius;
    protected final Geometry geometry;

    public Wheel(double radius, Geometry geometry) {
	this.radius = radius;
	this.geometry = geometry;
    }

    @Override
    public Category getCategory() {
	return Category.Vehicle;
    }

    @Override
    public Circle getBoundingCircle(double[] state) {
	return new Circle(getLocation(state), radius);
    }

    @Override
    public CollisionObject getCollisionObject(double[] state,
	    double[] derivatives, Vector2D point) {
	Vector2D location = getLocation(state);
	return new CollisionObject(radius, 1, location, location,
		Vector2D.getZeroVector(), 0, 0, null);
    }

    @Override
    public double getDistanceX(double xPosition, double[] state) {
	throw new RuntimeException("Not supported");
    }

    @Override
    public void getPointCollision(double[] state, double[] derivtives,
	    CollisionObject pointSource, List<CollisionMatrix> outList) {
	throw new RuntimeException("Not supported");
    }

    @Override
    public void draw(T graphics, double[] state, double offset) {
	// For now: nothing to be done
    }

    @Override
    public abstract VehiclePartTypes getType();

    protected abstract Vector2D getLocation(double[] state);

}
