package WorldObjects;

import java.util.List;

import Auxiliary.CollisionDynamicObjectBase;
import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.DynamicTypes;
import Auxiliary.ICallbackFunction;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Circle;

public abstract class DoublePendulumBase<T> implements IWorldDynamicObject<T> {

    protected int offset;

    private int g = 9;
    protected final Vector2D center;
    protected final int L1 = 100, L2 = 100, M1 = 1, M2 = 4;

    public DoublePendulumBase(Vector2D center) {
	this.center = center;
    }

    public void addExternalForce(double[] state, double[] outstate) {
    };
    
    @Override
    public void evaluateForce(double[] state, double[] derivatives) {
	double theta1 = state[offset];
	double theta2 = state[offset + 1];
	double theta1Dot = state[offset + 2];
	double theta2Dot = state[offset + 3];
	double denominator = L2
		* (2 * M1 + M2 - M2 * Math.cos(2 * theta1 - 2 * theta2));
	double numerator1 = -g * (2 * M1 + M2) * Math.sin(theta1)
		- M2 * g * Math.sin(theta1 - 2 * theta2)
		- 2 * Math.sin(theta1 - theta2) * M2
			* (theta2Dot * theta2Dot * L2 + theta1Dot * theta1Dot
				* L1 * Math.cos(theta1 - theta2));
	double numerator2 = 2 * Math.sin(theta1 - theta2)
		* (theta1Dot * theta1Dot * L1 * (M1 + M2)
			+ g * (M1 + M2) * Math.cos(theta1)
			+ theta2Dot * theta2Dot * L2 * M2
				* Math.cos(theta1 - theta2));

	derivatives[offset] += theta1Dot;
	derivatives[offset + 1] += theta2Dot;
	derivatives[offset + 2] += numerator1 / denominator;
	derivatives[offset + 3] += numerator2 / denominator;

    }

    @Override
    public CollisionObject getCollisionObject(double[] state,
	    double[] derivatives, Vector2D point) {
	double theta1 = state[offset];
	double theta2 = state[offset + 1];
	double theta1Dot = state[offset + 2];
	double theta2Dot = state[offset + 3];
	double sinTheta1 = Math.sin(theta1);
	double cosTheta1 = Math.cos(theta1);
	double sinTheta2 = Math.sin(theta2);
	double cosTheta2 = Math.cos(theta2);
	Vector2D p1 = new Vector2D(center.X + L1 * sinTheta1,
		center.Y + L1 * cosTheta1);
	Vector2D p2 = new Vector2D(p1.X + L2 * sinTheta2,
		p1.Y + L2 * cosTheta2);
	Vector2D location = p2;
	Vector2D velocity = new Vector2D(
		theta1Dot * L1 * cosTheta1 + theta2Dot * L2 * cosTheta2,
		-theta1Dot * L1 * sinTheta1 - theta2Dot * L2 * sinTheta2);
	return new CollisionObject(20, M2, location, location, velocity, 0,
		Double.POSITIVE_INFINITY, getCallback(state, derivatives,
			sinTheta1, cosTheta1, sinTheta2, cosTheta2));
    }

    @Override
    public void getPointCollision(double[] state, double[] derivtives,
	    CollisionObject pointSource, List<CollisionMatrix> outList) {
	throw new RuntimeException("Not supported");

    }

    @Override
    public abstract void draw(T graphics, double[] state, double offsetX);

    @Override
    public double[] initialState() {
	return new double[] { 1.5, 3.15 + 0.1 * Math.random(), 0, 0 };
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
	return DynamicTypes.DoublePendulum;
    }

    private ICallbackFunction getCallback(final double[] state,
	    final double[] derivatives, final double sinTheta1,
	    final double cosTheta1, final double sinTheta2,
	    final double cosTheta2) {
	return new ICallbackFunction() {

	    @Override
	    public void callbackFunctionImpulse(double distance,
		    Vector2D normal, double J) {
		Vector2D t2 = new Vector2D(-cosTheta2, sinTheta2);
		Vector2D n2 = new Vector2D(sinTheta2, cosTheta2);
		Vector2D t1 = new Vector2D(-cosTheta1, sinTheta1);
		state[offset + 2] -= n2.dot(normal) * n2.dot(t1) * J
			/ (M1 * L1);
		state[offset + 3] -= t2.dot(normal) * J / (M2 * L2);
	    }

	    @Override
	    public void callbackFunctionForce(Vector2D force) {

		// TODO Auto-generated method stub
	    }

	    @Override
	    public double callbackImpulseModifier(Vector2D normal) {
		Vector2D t2 = new Vector2D(-cosTheta2, sinTheta2);
		Vector2D n2 = new Vector2D(sinTheta2, cosTheta2);
		Vector2D t1 = new Vector2D(-cosTheta1, sinTheta1);
		return Math.pow(t2.dot(normal), 2) / M2
			+ Math.pow(n2.dot(normal) * n2.dot(t1), 2) / M1;
	    }
	};
    }

    @Override
    public Circle getBoundingCircle(double[] state) {
	double theta1 = state[offset];
	double theta2 = state[offset + 1];
	double sinTheta1 = Math.sin(theta1);
	double cosTheta1 = Math.cos(theta1);
	double sinTheta2 = Math.sin(theta2);
	double cosTheta2 = Math.cos(theta2);
	Vector2D location = new Vector2D(
		center.X + L1 * sinTheta1 + L2 * sinTheta2,
		center.Y + L1 * cosTheta1 + L2 * cosTheta2);
	return new Circle(location, 10);
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
