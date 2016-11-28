package WorldObjects;

import java.util.Collections;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObjectBase;
import Auxiliary.CollisionObjectBase.Type;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.StaticTypes;
import Auxiliary.IWorldStaticObject;
import Auxiliary.Vector2D;

public abstract class BarBase<T> implements IWorldStaticObject<T> {

    public final Vector2D A;
    public final Vector2D B;
    private final Vector2D tangent;
    private final Vector2D normal;
    private final double denominator;
    private final double maxPenetrationDepth = 30;
    private final CollisionObjectBase collisionObject;

    public BarBase(Vector2D A, Vector2D B) {
	this.A = A;
	this.B = B;
	Vector2D AB = B.subs(A);
	tangent = AB.scale(1. / AB.norm());
	normal = new Vector2D(tangent.Y, -tangent.X);
	denominator = AB.Y * normal.X - AB.X * normal.Y;
	collisionObject = new CollisionObjectBase(Double.POSITIVE_INFINITY,
		Double.POSITIVE_INFINITY, 0d,
		Collections.<Vector2D> emptyList(),
		Collections.singletonList(new Vector2D[] { B, A }), false,
		Type.STATIC);
    }

    @Override
    public abstract void draw(T graphics, double offset);

    @Override
    public CollisionObjectBase getCollisionObject() {
	return collisionObject;
    }

    @Deprecated
    public CollisionMatrix getPointCollision(CollisionObject object) {
	Vector2D AC = object.point.subs(A);
	double numerator = AC.Y * normal.X - AC.X * normal.Y;
	double x = numerator / denominator;
	if (x < 0 || x > 1)
	    return null;
	Vector2D AB = A.subs(B);
	numerator = AC.Y * AB.X - AC.X * AB.Y;
	double y = numerator / denominator;
	if (y < -maxPenetrationDepth || y > object.radius)
	    return null;
	Vector2D relativeVelocity = object.getPointVelocity();
	return new CollisionMatrix(object, CollisionObject.STATIC_OBJECT,
		normal, relativeVelocity, null, object.radius - y);
    }

    @Override
    public double getShortestDistanceX(double xPosition) {
	return (xPosition >= A.X && xPosition <= B.X) ? 0
		: Math.min(Math.abs(A.X - xPosition),
			Math.abs(B.X - xPosition));
    }

    @Override
    public StaticTypes getType() {
	return StaticTypes.Bar;
    }

    @Override
    public Category getCategory() {
	return Category.Static;
    }

    @Override
    public void draw(T graphics, double[] state, double offset) {
	draw(graphics, offset);
    }

}
