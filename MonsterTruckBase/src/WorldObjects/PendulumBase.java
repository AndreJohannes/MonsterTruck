package WorldObjects;

import java.util.List;

import Auxiliary.CollisionDynamicObjectBase;
import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObject.Type;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.DynamicTypes;
import Auxiliary.ICallbackFunction;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Circle;

public abstract class PendulumBase<T> implements IWorldDynamicObject<T> {

    protected int offset;
    protected final double radius;
    private final double mass = 100;
    protected final Vector2D center;

    public PendulumBase(Vector2D center, double radius) {
	this.center = center;
	this.radius = radius;
    }

    public void addExternalForce(double[] state, double[] outstate) {
    };

    @Override
    public void evaluateForce(double[] state, double[] derivatives) {
	double positionR = state[offset];
	double velocityR = state[offset + 1];

	derivatives[offset] += velocityR;
	derivatives[offset + 1] += 0.01 * Math.cos(positionR)
		- 0.001 * velocityR;
    }

    @Override
    public CollisionObject getCollisionObject(double[] state,
	    double[] derivatives, Vector2D dummyPoint) {
	double positionR = state[offset];
	double velocityR = state[offset + 1];
	double sin = Math.sin(positionR);
	double cos = Math.cos(positionR);
	Vector2D location = center.add(cos * radius, sin * radius);
	Vector2D velocity = new Vector2D(-sin * radius, cos * radius)
		.scale(velocityR);
	return new CollisionObject(20, mass, location, location, velocity, 0,
		Double.POSITIVE_INFINITY,
		getCallback(state, derivatives, sin, cos));
    }

    @Override
    public void getPointCollision(double[] state, double[] derivatives,
	    CollisionObject collisionObject, List<CollisionMatrix> outList) {
	double positionR = state[offset];
	double sin = Math.sin(positionR);
	double cos = Math.cos(positionR);
	Vector2D location = center.add(cos * radius, sin * radius);
	Vector2D AB = collisionObject.point.subs(location);
	double distance = AB.norm();
	if (distance > (collisionObject.radius + 20))
	    return;
	CollisionObject thisObject = getCollisionObject(state, derivatives,
		null);
	Vector2D relativeVelocity = collisionObject.type == Type.DYNAMIC
		? (thisObject.getPointVelocity(/* invert= */true))
			.add(collisionObject.getPointVelocity())
		: thisObject.getPointVelocity(true);

	outList.add(new CollisionMatrix(collisionObject, thisObject,
		AB.scale(1 / distance), relativeVelocity, null,
		20 + collisionObject.radius - distance));
    }

    @Override
    public abstract void draw(T graphics, double[] state, double offsetX);

    @Override
    public double[] initialState() {
	return new double[] { -Math.PI / 2, -0.1 };
    }

    @Override
    public void setOffset(int offset) {
	this.offset = offset;
    }

    @Override
    public int getOffset() {
	return offset;
    }

    @Override
    public double getDistanceX(double xPosition, double[] state) {
	return Math.abs(center.X - xPosition);
    }

    @Override
    public DynamicTypes getType() {
	return DynamicTypes.Pendulum;
    }

    private ICallbackFunction getCallback(final double[] state,
	    final double[] derivatives, final double sin, final double cos) {
	return new ICallbackFunction() {

	    @Override
	    public void callbackFunctionImpulse(double distance,
		    Vector2D normal, double J) {
		state[offset + 1] += normal.dot(new Vector2D(-sin, cos)) * J
			/ (mass * radius);
	    }

	    @Override
	    public void callbackFunctionForce(Vector2D force) {
	    }

	    @Override
	    public double callbackImpulseModifier(Vector2D normal) {
		Vector2D t = new Vector2D(sin, -cos);
		return Math.pow(t.dot(normal), 2) / mass;
	    }
	};

    }

    @Override
    public Circle getBoundingCircle(double[] state) {
	double positionR = state[offset];
	double sin = Math.sin(positionR);
	double cos = Math.cos(positionR);
	Vector2D location = center.add(cos * radius, sin * radius);
	return new Circle(location, radius);
    }

    @Override
    public Category getCategory() {
	return Category.Dynamic;
    }
    
    public CollisionDynamicObjectBase getCollisionObject(double[] state,
	    double[] derivatives){
	return null;
    };

}
