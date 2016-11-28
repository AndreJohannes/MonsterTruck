package Auxiliary;

import java.util.LinkedList;
import java.util.List;

import Auxiliary.CollisionObjectBase.CollisionPoint;

public abstract class CollisionDynamicObjectBase extends CollisionObjectBase {

    protected Vector2D getPointVelocity(Vector2D point) {
	double rX = point.X - center.X;
	double rY = point.Y - center.Y;
	return new Vector2D(velocity.X + angularVelocity * rY,
		velocity.Y - angularVelocity * rX);
    }

    protected Vector2D center;

    protected boolean expired = true;

    public CollisionDynamicObjectBase(double mass, double momentOfInertia,
	    double radius,boolean isOverruling) {
	super(mass, momentOfInertia, radius, new LinkedList<Vector2D>(),
		new LinkedList<Vector2D[]>(), isOverruling, Type.DYNAMIC);
    }

    public abstract void addImpulse(double J, Vector2D point, Vector2D normal,
	    double distance, boolean rest);

    public abstract void applyAndReset(double[] state, double[] derivatives);

    public abstract boolean atRest();

    protected abstract void setValues(double[] state, double[] derivatives);

    public synchronized CollisionDynamicObjectBase get(double[] state,
	    double[] derivatives) {
	if (expired) {
	    setValues(state, derivatives);
	    expired = false;
	}
	return this;
    }

}
