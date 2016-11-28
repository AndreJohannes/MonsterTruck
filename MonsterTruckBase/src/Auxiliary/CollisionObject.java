package Auxiliary;

public class CollisionObject {

    public enum Type {
        STATIC, DYNAMIC;
    }

    public final double mass;
    public final double radius;
    public final double momentumOfInertia;
    public final double velocityR;
    public final Vector2D velocity;
    public final Vector2D point;
    public final Vector2D center;
    public final ICallbackFunction callbackFunction;
    public final Type type;

    @Deprecated
    public CollisionObject(double radius, double mass, Vector2D point,
                           Vector2D center, Vector2D velocity, double angularVelocity,
                           double momentumOfInertia, ICallbackFunction callbackFunction) {
        this.radius = radius;
        this.mass = mass;
        this.point = point;
        this.center = center;
        this.velocity = velocity;
        this.velocityR = angularVelocity;
        this.momentumOfInertia = momentumOfInertia;
        this.type = Type.DYNAMIC;
        this.callbackFunction = callbackFunction;
    }

    public CollisionObject(double radius, Vector2D point, Vector2D center) {
        this.radius = radius;
        this.mass = Double.POSITIVE_INFINITY;
        this.point = point;
        this.center = center;
        this.velocity = Vector2D.getZeroVector();
        this.velocityR = 0;
        this.momentumOfInertia = 0;
        this.type = Type.STATIC;
        this.callbackFunction = null;
    }

    public Vector2D getPointVelocity(boolean invert) {
        // TODO: once it was calculated we can store it in a field
        double rX = point.X - center.X;
        double rY = point.Y - center.Y;
        return invert
                ? new Vector2D(-velocity.X - velocityR * rY,
                -velocity.Y + velocityR * rX)
                : new Vector2D(velocity.X + velocityR * rY,
                velocity.Y - velocityR * rX);
    }

    public Vector2D getPointVelocity() {
        return getPointVelocity(false);
    }

    public static final CollisionObject STATIC_OBJECT = new CollisionObject();

    private CollisionObject() {
        this.radius = 0;
        this.mass = Double.POSITIVE_INFINITY;
        this.point = null;
        this.center = null;
        this.velocity = Vector2D.getZeroVector();
        this.velocityR = 0;
        this.momentumOfInertia = 0;
        this.type = Type.STATIC;
        this.callbackFunction = null;
    }

}
