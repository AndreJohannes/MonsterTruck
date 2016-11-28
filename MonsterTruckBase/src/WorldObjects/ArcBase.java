package WorldObjects;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObjectBase;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.StaticTypes;
import Auxiliary.IWorldStaticObject;
import Auxiliary.MathHelpers;
import Auxiliary.Vector2D;
import Auxiliary.CollisionObjectBase.Type;

public abstract class ArcBase<T> implements IWorldStaticObject<T> {

    private static class ArcCollisionObject extends CollisionObjectBase {

	private final Vector2D center;
	private final boolean inverse;
	protected final double startAngle;
	protected final double arcAngle;

	public ArcCollisionObject(double radius, List<Vector2D> vertices,
		double startAngle, double arcAngle) {
	    super(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, radius,
		    vertices, Collections.<Vector2D[]> emptyList(), true,
		    Type.STATIC);
	    this.center = vertices.get(0);
	    this.inverse = false;
	    this.startAngle = startAngle;
	    this.arcAngle = arcAngle;
	}

	@Override
	protected List<CollisionPoint> getCollisionPoints(
		CollisionObjectBase object, boolean reverseNormal) {
	    LinkedList<CollisionPoint> collisionList = new LinkedList<CollisionPoint>();
	    for (Vector2D vertex : getVertices(object)) {
		CollisionPoint normalAndDistance = getCollisionPoint(vertex,
			getRadius(object), reverseNormal);
		if (normalAndDistance != null)
		    collisionList.add(normalAndDistance);
	    }
	    for (CollisionPoint collisionPoint : collisionList) {
		double J = reverseNormal
			? calculateJ(object, this, collisionPoint)
			: calculateJ(this, object, collisionPoint);
		collisionPoint.setJ(J);
	    }
	    return collisionList;
	}

	protected CollisionPoint getCollisionPoint(Vector2D point,
		double objectRadius, boolean reverseNormal) {
	    Vector2D AB = point.subs(center);
	    double length = AB.norm();
	    if (length > radius + objectRadius
		    || length < radius - objectRadius)
		return null;
	    // TODO: Get rid of three tertiary operators
	    double diffAngle = MathHelpers.mod((inverse ? -1 : 1)
		    * (Math.toDegrees(Math.atan2(-AB.Y, AB.X)) - startAngle),
		    360);
	    if (diffAngle < 0 || diffAngle > (inverse ? -1 : 1) * arcAngle)
		return null;
	    return new CollisionPoint(point,
		    AB.scale((reverseNormal ? 1 : -1) * (inverse ? 1. : -1.)
			    / length),
		    objectRadius - (inverse ? -1 : 1) * (radius - length));
	}

    }

    protected final Vector2D center;
    protected final double radius;
    protected final double startAngle;
    protected final double arcAngle;
    protected final boolean inverse;
    protected final CollisionObjectBase collisionObject;

    public ArcBase(Vector2D center, double radius, double startAngle,
	    double arcAngle) {
	this.center = center;
	this.radius = radius;
	this.startAngle = startAngle;
	this.arcAngle = arcAngle;
	this.inverse = (arcAngle < 0);
	this.collisionObject = inverse ? new CollisionObjectBase(
		Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, radius,
		Collections.singletonList(center),
		Collections.<Vector2D[]> emptyList(), false, Type.STATIC)
		: new ArcCollisionObject(radius,
			Collections.singletonList(center), startAngle,
			arcAngle);
    }

    public abstract void draw(T gr, double offset);

    public CollisionObject getCollisionObjectOld() {
	if (!inverse)
	    throw new RuntimeException("Not supported for non inverse Arcs");
	return new CollisionObject(radius, center, center);
    }

    @Override
    @Deprecated
    public CollisionMatrix getPointCollision(CollisionObject object) {
	Vector2D AB = object.point.subs(center);
	double length = AB.norm();
	if (length > radius + object.radius || length < radius - object.radius)
	    return null;
	// TODO: Get rid of three tertiary operators
	double diffAngle = MathHelpers.mod((inverse ? -1 : 1)
		* (Math.toDegrees(Math.atan2(-AB.Y, AB.X)) - startAngle), 360);
	if (diffAngle < 0 || diffAngle > (inverse ? -1 : 1) * arcAngle)
	    return null;
	Vector2D relativeVelocity = object.getPointVelocity();
	return new CollisionMatrix(object, CollisionObject.STATIC_OBJECT,
		AB.scale((inverse ? 1. : -1.) / length), relativeVelocity, null,
		object.radius - (inverse ? -1 : 1) * (radius - length));
    }

    @Override
    public double getShortestDistanceX(double xPosition) {
	double A = center.X - radius;
	double B = center.X + radius;
	return (A < xPosition ? (B > xPosition ? 0 : xPosition - B)
		: A - xPosition);
    }

    @Override
    public StaticTypes getType() {
	return StaticTypes.Arc;
    }

    public boolean isInverse() {
	return inverse;
    }

    @Override
    public Category getCategory() {
	return Category.Static;
    }

    @Override
    public void draw(T graphics, double[] state, double offset) {
	draw(graphics, offset);
    }

    @Override
    public CollisionObjectBase getCollisionObject() {
	return collisionObject;
    }

}
