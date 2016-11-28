package VehicleObjects;

import java.util.LinkedList;
import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObject.Type;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.VehiclePartTypes;
import Auxiliary.ICallbackFunction;
import Auxiliary.IVehicleObject;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Circle;
import Auxiliary.Vector2D.Line;
import VehicleObjects.VehicleBase.Geometry;

public abstract class Bumper<T> implements IVehicleObject<T> {

    public static class FrontBumper<T> extends Bumper<T> {

	public FrontBumper(Geometry geometry, VehicleBase<T> vehicle) {
	    super(geometry, vehicle);
	}

	@Override
	public VehiclePartTypes getType() {
	    return VehiclePartTypes.FrontBumper;
	}

	@Override
	protected Line getLine(double[] state) {
	    double rad = state[4];
	    double sin = Math.sin(rad), cos = Math.cos(rad);
	    return new Line(geometry.CMtoFrontBumper.A.rotate(sin, cos),
		    geometry.CMtoFrontBumper.B.rotate(sin, cos));
	}

    }

    public static class RearBumper<T> extends Bumper<T> {

	public RearBumper(Geometry geometry, VehicleBase<T> vehicle) {
	    super(geometry, vehicle);
	}

	@Override
	public VehiclePartTypes getType() {
	    return VehiclePartTypes.RearBumper;
	}

	@Override
	protected Line getLine(double[] state) {
	    double rad = state[4];
	    double sin = Math.sin(rad), cos = Math.cos(rad);
	    return new Line(geometry.CMtoRearBumper.A.rotate(sin, cos),
		    geometry.CMtoRearBumper.B.rotate(sin, cos));
	}

    }

    protected final Geometry geometry;
    private final VehicleBase<T> vehicle;

    public Bumper(Geometry geometry, VehicleBase<T> vehicle) {
	this.geometry = geometry;
	this.vehicle = vehicle;
    }

    @Override
    public Circle getBoundingCircle(double[] state) {
	Line line = getLine(state);
	double radius = line.A.distance(line.B);// TODO: could be precomputed
	Vector2D center = line.A.add(line.B).scale(0.5)
		.add(new Vector2D(state[0], state[1]));
	return new Circle(center, radius);
    }

    @Override
    public Category getCategory() {
	return Category.Vehicle;
    }

    @Override
    public CollisionObject getCollisionObject(double[] state,
	    double[] derivatives, Vector2D point) {
	throw new RuntimeException("Not supported");
    }

    @Override
    public double getDistanceX(double xPosition, double[] state) {
	throw new RuntimeException("Not supported");
    }

    @Override
    public void getPointCollision(double[] state, double[] derivatives,
	    CollisionObject collisionObject, List<CollisionMatrix> outList) {

	Vector2D position = new Vector2D(state[0], state[1]);

	Line line = getLine(state);

	Vector2D A = line.A.add(position);
	Vector2D B = line.B.add(position);

	CollisionMatrix collisionMatrix = getVertexCollision(A, collisionObject,
		state, derivatives);

	if (collisionMatrix != null)
	    outList.add(collisionMatrix);

	collisionMatrix = getVertexCollision(B, collisionObject, state,
		derivatives);

	if (collisionMatrix != null)
	    outList.add(collisionMatrix);

	collisionMatrix = getEdgeCollision(A, B, collisionObject, state,
		derivatives);

	if (collisionMatrix != null)
	    outList.add(collisionMatrix);

    }

    @Override
    public abstract VehiclePartTypes getType();

    public List<CollisionObject> getVertexList(double[] state,
	    double[] derivatives) {
	Vector2D position = new Vector2D(state[0], state[1]);

	double velocityX = state[2];
	double velocityY = state[3];
	double velocityR = state[5];

	List<CollisionObject> retList = new LinkedList<CollisionObject>();

	Line line = getLine(state);

	retList.add(getVertex(line.A, position, velocityX, velocityY, velocityR,
		state, derivatives));

	retList.add(getVertex(line.B, position, velocityX, velocityY, velocityR,
		state, derivatives));

	return retList;
    }

    private ICallbackFunction getCallback(final double[] state,
	    final double[] derivatives, final Vector2D x) {
	return new ICallbackFunction() {

	    @Override
	    public void callbackFunctionForce(Vector2D force) {
		// derivatives[offset + 2] += 1 * force.X / mass;
		// derivatives[offset + 3] += 1 * force.Y / mass;
		// derivatives[offset + 5] += 1 * (x.Y * force.X - x.X *
		// force.Y)
		// / (momentOfInertia);
	    }

	    @Override
	    public void callbackFunctionImpulse(double distance,
		    Vector2D normal, double J) {
		// incHealth((J > 5 ? -0.1 * J : 0));
		state[2] += J * normal.X / geometry.mass;
		state[3] += J * normal.Y / geometry.mass;
		state[5] += (-x.X * J * normal.Y + x.Y * J * normal.X)
			/ geometry.momentOfInertia;
		// distance = Math.max(-1, distance);
		// distance = Math.min(1, distance);
		// state[offset] += distance * normal.X;
		// state[offset + 1] += distance * normal.Y;
	    }

	    @Override
	    public double callbackImpulseModifier(Vector2D normal) {
		throw new RuntimeException("Method not implmented");
	    }
	};
    }

    // TODO: almost code duplication!!
    private CollisionMatrix getEdgeCollision(Vector2D A, Vector2D B,
	    CollisionObject collisionObject, double[] state,
	    double[] derivatives) {
	Vector2D AB = A.subs(B);
	Vector2D AC = collisionObject.point.subs(A);
	Vector2D tangent = AB.scale(1. / AB.norm());
	Vector2D normal = new Vector2D(tangent.Y, -tangent.X);
	double maxPenetrationDepth = 20;
	double denominator = AB.Y * normal.X - AB.X * normal.Y;
	double numerator = AC.Y * normal.X - AC.X * normal.Y;
	double x = -numerator / denominator;
	if (x < 0 || x > 1)
	    return null;
	numerator = AC.Y * AB.X - AC.X * AB.Y;
	double y = -numerator / denominator;
	if (y < -maxPenetrationDepth || y > collisionObject.radius + 1)
	    return null;

	Vector2D point = A.add(AB.scale(-x));
	Vector2D center = new Vector2D(state[0], state[1]);
	Vector2D velocity = new Vector2D(state[2], state[3]);
	double angularVelocity = state[5];
	CollisionObject thisObject = new CollisionObject(1, geometry.mass,
		point, center, velocity, angularVelocity,
		geometry.momentOfInertia,
		getCallback(state, derivatives, point.subs(center)));
	Vector2D relativeVelocity = collisionObject.type == Type.DYNAMIC
		? (thisObject.getPointVelocity(/* invert= */true))
			.add(collisionObject.getPointVelocity())
		: thisObject.getPointVelocity(/* invert= */true);

	return new CollisionMatrix(collisionObject, thisObject, normal,
		relativeVelocity, null,
		collisionObject.radius + thisObject.radius - y);

    }

    private CollisionObject getVertex(final Vector2D vertex, Vector2D center,
	    double velocityX, double velocityY, double velocityR,
	    double[] state, double[] derivatives) {
	return new CollisionObject(1, geometry.mass, vertex.add(center), center,
		new Vector2D(velocityX, velocityY), velocityR,
		geometry.momentOfInertia,
		getCallback(state, derivatives, vertex));
    }

    private CollisionMatrix getVertexCollision(Vector2D A,
	    CollisionObject collisionObject, double[] state,
	    double[] derivatives) {
	double distance = A.distance(collisionObject.point);
	if (distance > 1 + collisionObject.radius)
	    return null;

	Vector2D point = A;
	Vector2D center = new Vector2D(state[0], state[1]);
	Vector2D velocity = new Vector2D(state[2], state[3]);
	double angularVelocity = state[5];
	CollisionObject thisObject = new CollisionObject(1, geometry.mass,
		point, center, velocity, angularVelocity,
		geometry.momentOfInertia,
		getCallback(state, derivatives, point.subs(center)));

	Vector2D relativeVelocity = collisionObject.type == Type.DYNAMIC
		? (thisObject.getPointVelocity(true))
			.add(collisionObject.getPointVelocity())
		: thisObject.getPointVelocity(true);
	;
	Vector2D AB = A.subs(collisionObject.point);

	return new CollisionMatrix(collisionObject, thisObject,
		AB.scale(-1 / distance), relativeVelocity, null,
		1 + collisionObject.radius - distance);
    }

    private void incHealth(double value) {
	vehicle.incHealth(value);
    }
    
    @Override
    public void draw(T graphics, double[] state, double offset) {
	    // For now: nothing to be done
	}

    protected abstract Line getLine(double[] state);

}
