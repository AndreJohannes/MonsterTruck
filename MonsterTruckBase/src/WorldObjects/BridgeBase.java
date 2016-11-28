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

public abstract class BridgeBase<T> implements IWorldDynamicObject<T> {

    public static class Suspension {
	private final double A, B, C;

	public Suspension(double A) {
	    this.A = A;
	    this.B = 0;
	    this.C = 0;
	}

	public Suspension(double A, double B, double C) {
	    this.A = A;
	    this.B = B;
	    this.C = C;
	}

	public double function(double x) {
	    return A + B * Math.pow(x - C, 2);
	}

    }

    public class XYN {
	private final double X;
	private final double Y;
	private final Vector2D N;

	public XYN(double X, double Y, Vector2D N) {
	    this.X = X;
	    this.Y = Y;
	    this.N = N;
	}
    }

    protected final Vector2D startPoint;
    protected final Vector2D endPoint;
    private final double springLength;
    protected final int numberOfLinks;
    protected int offset;
    protected final Suspension suspension;
    private final Circle boundingCircle;

    public BridgeBase(Vector2D startPoint, Vector2D endPoint, int numberOfLinks,
	    Suspension suspension) {
	this.startPoint = startPoint;
	this.endPoint = endPoint;
	this.numberOfLinks = numberOfLinks;
	springLength = endPoint.subs(startPoint).norm() / (1.5 * numberOfLinks);
	this.suspension = suspension;
	boundingCircle = new Circle(startPoint.add(endPoint).scale(0.5),
		0.5 * startPoint.distance(endPoint));
    }

    public abstract void draw(T graphics, double[] state, double offsetX);

    @Override
    public void addExternalForce(double[] state, double[] outstate) {
    };

    public void evaluateForce(double[] state, double[] outstate) {
	boolean first = true;
	Vector2D previousPoint = startPoint;
	int offset = this.offset;
	for (int i = 0; i < (numberOfLinks - 1); i++) {
	    Vector2D point = new Vector2D(state[offset], state[offset + 1]);
	    Vector2D velocity = new Vector2D(state[offset + 2],
		    state[offset + 3]);
	    Vector2D link = point.subs(previousPoint);
	    double length = link.norm();
	    Vector2D force = link.scale(-(1 - springLength / length));
	    outstate[offset] += velocity.X;
	    outstate[offset + 1] += velocity.Y;
	    outstate[offset + 2] += (130 * force.X)
		    - 0.01 * Math.pow(velocity.X, 3);
	    outstate[offset + 3] += (130 * force.Y) + 100
		    - 0.01 * Math.pow(velocity.Y, 3);
	    if (first == false) {
		outstate[offset - 2] -= 130 * force.X;
		outstate[offset - 1] -= 130 * force.Y - 100;
	    }
	    first = false;
	    previousPoint = point;
	    offset += 4;
	}
	Vector2D link = previousPoint.subs(endPoint);
	double length = link.norm();
	Vector2D force = link.scale(-(1 - springLength / length));
	outstate[offset - 2] += 130 * force.X;
	outstate[offset - 1] += 130 * force.Y + 100;
    }

    @Override
    public CollisionObject getCollisionObject(double[] state,
	    double[] derivatives, Vector2D point) {
	return null;
    }

    @Override
    public double getDistanceX(double xPosition, double[] state) {
	return Math.min(Math.abs(startPoint.X - xPosition),
		Math.abs(endPoint.X - xPosition));
    }

    @Override
    public int getOffset() {
	return offset;
    }

    @Override
    public void getPointCollision(double[] state, double[] derivatives,
	    CollisionObject collisionObject, List<CollisionMatrix> outList) {
	Vector2D B = startPoint;
	boolean first = true;
	int offset = this.offset;
	for (int i = 0; i < (numberOfLinks - 1); i++) {
	    Vector2D A = B;
	    B = new Vector2D(state[offset], state[offset + 1]);
	    XYN xyn = getXYN(A, B, collisionObject.point,
		    collisionObject.radius);
	    if (xyn != null) {
		// double velocityAX = first ? 0 : state[offset - 2];
		// double velocityAY = first ? 0 : state[offset - 1];
		// double velocityBX = state[offset + 2];
		// double velocityBY = state[offset + 3];
		// Vector2D velocity = new Vector2D((1 - xyn.X) * velocityAX +
		// xyn.X * velocityBX,
		// (1 - xyn.X) * velocityAY + xyn.X * velocityBY);
		outList.add(
			new CollisionMatrix(collisionObject,
				getCollisionObject(getCallback(
					first ? null : offset - 4, offset,
					0.5 + 0 * xyn.X, state, derivatives)),
			xyn.N, collisionObject.velocity, null, xyn.Y));
	    }
	    offset += 4;
	    first = false;
	}
	Vector2D A = B;
	XYN xyn = getXYN(A, endPoint, collisionObject.point,
		collisionObject.radius);
	if (xyn != null) {
	    // double velocityAX = state[offset - 2];
	    // double velocityAY = state[offset - 1];
	    // Vector2D velocity = new Vector2D((1 - xyn.X) * velocityAX,
	    // (1 - xyn.X) * velocityAY);
	    outList.add(new CollisionMatrix(collisionObject,
		    getCollisionObject(getCallback(offset - 4, null, xyn.X,
			    state, derivatives)),
		    xyn.N, collisionObject.velocity, null, xyn.Y));
	}
    }

    @Override
    public DynamicTypes getType() {
	return DynamicTypes.Bridge;
    }

    public double[] initialState() {
	double returnArray[] = new double[4 * numberOfLinks];
	Vector2D AB = endPoint.subs(startPoint);
	int offset = 0;
	for (int i = 1; i < (numberOfLinks); i++) {
	    Vector2D point = startPoint
		    .add(AB.scale((i) / (1.0 * numberOfLinks)));
	    returnArray[offset] = point.X;
	    returnArray[offset + 1] = point.Y;
	    offset += 4;
	}
	return returnArray;
    }

    @Override
    public void setOffset(int offset) {
	this.offset = offset;
    }

    private ICallbackFunction getCallback(final Integer offset1,
	    final Integer offset2, final double frac, final double[] state,
	    final double[] derivatives) {
	return new ICallbackFunction() {

	    @Override
	    public void callbackFunctionForce(Vector2D force) {
		// force = Vector2D.getZeroVector();
		if (offset1 != null) {
		    derivatives[offset1 + 2] += 13 * (1 - frac) * force.X;
		    derivatives[offset1 + 3] += 13 * (1 - frac) * force.Y;
		}
		if (offset2 != null) {
		    derivatives[offset2 + 2] += 13 * (frac) * force.X;
		    derivatives[offset2 + 3] += 13 * (frac) * force.Y;
		}
	    }

	    @Override
	    public void callbackFunctionImpulse(double distance,
		    Vector2D normal, double J) {
		// TODO: decide what to do here
	    }

	    @Override
	    public double callbackImpulseModifier(Vector2D normal) {
		throw new RuntimeException("Method not implmented");
	    }
	};
    }

    private CollisionObject getCollisionObject(
	    ICallbackFunction callbackFunction) {
	return new CollisionObject(0d, 0d, null, null, null, 0d, 0d,
		callbackFunction);
    }

    private XYN getXYN(Vector2D A, Vector2D B, Vector2D C, double threshold) {
	Vector2D AB = B.subs(A);
	Vector2D AC = C.subs(A);
	double length = AB.norm();
	Vector2D normal = new Vector2D(AB.Y / length, -AB.X / length);
	double denominator = AB.Y * normal.X - AB.X * normal.Y; // TODO: can be
	// simplified
	double numerator = AC.Y * normal.X - AC.X * normal.Y;
	double x = numerator / denominator;
	if (x > 0 && x < 1) {
	    numerator = AC.Y * AB.X - AC.X * AB.Y;
	    double y = -numerator / denominator;
	    if (y > 0 && y < threshold) {
		return new XYN(x, (threshold - y), normal);
	    }
	}
	return null;
    }

    @Override
    public Circle getBoundingCircle(double[] state) {
	return boundingCircle;
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
