package Auxiliary;

public class CollisionMatrix {

    public CollisionObject collisionObject1;
    public CollisionObject collisionObject2;

    public final Vector2D normal;
    public final Vector2D relativeVelocity;
    public final Vector2D contactPoint;
    public final double distance;
    public static double HARDNESS = 1000;

    @Deprecated
    public CollisionMatrix(CollisionObject object1, CollisionObject object2,
	    Vector2D normal, Vector2D relativeVelocity, Vector2D contactPoint,
	    double distance) {
	this.collisionObject1 = object1;
	this.collisionObject2 = object2;
	this.normal = normal;
	this.relativeVelocity = relativeVelocity;
	this.contactPoint = contactPoint;
	this.distance = distance;
    }

}
