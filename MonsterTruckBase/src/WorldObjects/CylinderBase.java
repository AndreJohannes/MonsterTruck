package WorldObjects;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

public abstract class CylinderBase<T> implements IWorldDynamicObject<T> {

    private static class CylinderCollisionObject<T>
            extends CollisionDynamicObjectBase {

        private double dX = 0;
        private double dY = 0;
        private double dVx = 0;
        private double dVy = 0;
        private final CylinderBase<T> outer;

        public CylinderCollisionObject(CylinderBase<T> cylinder) {
            super(cylinder.mass, Double.POSITIVE_INFINITY, cylinder.radius, false);
            this.outer = cylinder;
        }

        @Override
        public synchronized void addImpulse(double J, Vector2D point, Vector2D normal,
                                            double distance, boolean rest) {

            dVx = J * normal.X / mass;
            dVy = J * normal.Y / mass;

        }

        @Override
        public void applyAndReset(double[] state, double[] derivatives) {
            expired = true;
            state[outer.offset + 0] += dX;
            state[outer.offset + 1] += dY;
            state[outer.offset + 2] += dVx;
            state[outer.offset + 3] += dVy;
            dX = 0;
            dY = 0;
            dVx = 0;
            dVy = 0;
            vertices.clear();
        }

        @Override
        public boolean atRest() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        protected void setValues(double[] state, double[] derivatives) {

            vertices.add(new Vector2D(state[outer.offset], state[outer.offset + 1]));

        }

    }

    private static double GRAVITY = 13;
    private static double FRICTION = 0.1;
    private static double ELASTICITY = 1.5;
    private static double HARDNESS = 2000;
    private final Vector2D startPosition;

    protected final double radius;
    private final double mass = 1d;
    protected int offset;
    protected final CollisionDynamicObjectBase _collisionObject;

    public CylinderBase(Vector2D center, double radius) {
        this.radius = radius;
        this.startPosition = center;
        this._collisionObject = new CylinderCollisionObject<T>(this);
    }

    @Override
    public abstract void draw(T graphics, double[] state, double offsetX);

    public void addExternalForce(double[] state, double[] outstate) {};

    @Override
    public void evaluateForce(double[] state, double[] derivatives) {
        double positionX = state[offset];
        double positionY = state[offset + 1];
        double velocityX = state[offset + 2];
        double velocityY = state[offset + 3];
        double angularMomentum = state[offset + 5];
        derivatives[offset] += velocityX;
        derivatives[offset + 1] += velocityY;
        derivatives[offset + 2] += -FRICTION * velocityX;
        derivatives[offset + 3] += GRAVITY - FRICTION * velocityY;
        derivatives[offset + 4] += angularMomentum;
    }

    private Circle boundingCirceCache = null;

    @Override
    public Circle getBoundingCircle(double[] state) {
        if (boundingCirceCache != null
                && (state[offset] == boundingCirceCache.A.X
                && state[offset + 1] == boundingCirceCache.A.Y))
            return boundingCirceCache;
        boundingCirceCache = new Circle(
                new Vector2D(state[offset], state[offset + 1]), radius);
        return boundingCirceCache;
    }

    private AtomicReference<CollisionObject> collisionObjectCache = new AtomicReference<CollisionObject>(
            null);

    @Override
    public CollisionObject getCollisionObject(double[] state,
                                              double[] derivatives, Vector2D dummyPoint) {
        CollisionObject collisionObjectCache = this.collisionObjectCache.get();
        if (collisionObjectCache != null) {
            if (collisionObjectCache.center.X == state[offset]
                    && collisionObjectCache.center.Y == state[offset + 1]
                    && collisionObjectCache.velocity.X == state[offset + 2]
                    && collisionObjectCache.velocity.Y == state[offset + 3]) {
                return collisionObjectCache;
            }
        }
        Vector2D location = new Vector2D(state[offset], state[offset + 1]);
        Vector2D velocity = new Vector2D(state[offset + 2], state[offset + 3]);
        collisionObjectCache = new CollisionObject(radius, mass, location,
                location, velocity, 0, Double.POSITIVE_INFINITY,
                getCallback(state, derivatives));
        this.collisionObjectCache.set(collisionObjectCache);
        return collisionObjectCache;
    }

    @Override
    public double getDistanceX(double xPosition, double[] state) {
        return Math.abs(state[offset] - xPosition);
    }

    public Vector2D getLocation(double state[]) {
        return new Vector2D(state[offset], state[offset + 1]);
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void getPointCollision(double[] state, double[] derivatives,
                                  CollisionObject collisionObject, List<CollisionMatrix> outList) {
        Vector2D AB = collisionObject.point
                .subs(new Vector2D(state[offset], state[offset + 1]));
        double distance = AB.norm();
        if (distance > (collisionObject.radius + radius))
            return;
        CollisionObject thisObject = getCollisionObject(state, derivatives,
                null);
        Vector2D relativeVelocity = collisionObject.type == Type.DYNAMIC
                ? (thisObject.getPointVelocity(/* invert= */true))
                .add(collisionObject.getPointVelocity())
                : thisObject.getPointVelocity(true);

        outList.add(new CollisionMatrix(collisionObject, thisObject,
                AB.scale(1 / distance), relativeVelocity, null,
                radius + collisionObject.radius - distance));
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public DynamicTypes getType() {
        return DynamicTypes.Cylinder;
    }

    @Override
    public double[] initialState() {
        return new double[]{startPosition.X, startPosition.Y, 0, 0, 0, 1};
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    private ICallbackFunction getCallback(final double[] state,
                                          final double[] derivatives) {
        return new ICallbackFunction() {

            @Override
            public void callbackFunctionForce(Vector2D force) {
                derivatives[offset + 2] += 10 * force.X / mass;
                derivatives[offset + 3] += 10 * force.Y / mass;
            }

            @Override
            public void callbackFunctionImpulse(double distance,
                                                Vector2D normal, double J) {
                state[offset + 2] += J * normal.X / mass;
                state[offset + 3] += J * normal.Y / mass;
                state[offset] += distance * normal.X;
                state[offset + 1] += distance * normal.Y;
            }

            @Override
            public double callbackImpulseModifier(Vector2D normal) {
                throw new RuntimeException("Method not implmented");
            }
        };
    }

    @Override
    public Category getCategory() {
        return Category.Dynamic;
    }

    public CollisionDynamicObjectBase getCollisionObject(double[] state,
                                                         double[] derivatives) {
        return null;
    }

    ;

}
