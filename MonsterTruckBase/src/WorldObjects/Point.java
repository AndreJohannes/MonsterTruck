package WorldObjects;

import java.util.Collections;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObjectBase;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.StaticTypes;
import Auxiliary.IWorldStaticObject;
import Auxiliary.Vector2D;
import Auxiliary.CollisionObjectBase.Type;

public class Point<T> implements IWorldStaticObject<T> {

    private final Vector2D A;
    private final CollisionObjectBase collisionObject;

    public Point(Vector2D A) {
        this.A = A;
        this.collisionObject = new CollisionObjectBase(Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY, 0d,
                Collections.singletonList(A),
                Collections.<Vector2D[]>emptyList(), false,
                Type.STATIC);
    }

    @Override
    public void draw(T graphics, double offset) {
        // Nothing to draw
    }

    public Vector2D getLocation() {
        return A;
    }

    @Override
    @Deprecated
    public CollisionMatrix getPointCollision(CollisionObject object) {
        double distance = object.point.subs(A).norm();
        if (distance > object.radius)
            return null;
        Vector2D AC = object.point.subs(A);
        Vector2D normal = AC.scale(1.0 / AC.norm());
        Vector2D relativeVelocity = object.getPointVelocity();
        return new CollisionMatrix(object, CollisionObject.STATIC_OBJECT,
                normal, relativeVelocity, null, -distance + object.radius);
    }

    @Override
    public double getShortestDistanceX(double xPosition) {
        return Math.abs(this.A.X - xPosition);
    }

    @Override
    public StaticTypes getType() {
        return StaticTypes.Point;
    }

    @Override
    public Category getCategory() {
        return Category.Static;
    }

    @Override
    public void draw(T graphics, double[] state, double offset) {
        // Nothing to be done
    }

    @Override
    public CollisionObjectBase getCollisionObject() {
        return collisionObject;
    }

}
