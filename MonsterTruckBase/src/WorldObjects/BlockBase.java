package WorldObjects;

import java.util.LinkedList;
import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObject.Type;
import Auxiliary.CollisionDynamicObjectBase;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.DynamicTypes;
import Auxiliary.ICallbackFunction;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Circle;

public abstract class BlockBase<T> implements IWorldDynamicObject<T> {

    private static class BlockCollisionObject<T> extends CollisionDynamicObjectBase {

        public BlockCollisionObject(BlockBase<T> block) {
            super(block.mass, block.momentOfInertia, block.cornerRadius, false);
            this.outer = block;
        }

        private double dX = 0;
        private double dY = 0;
        private double dVx = 0;
        private double dVy = 0;
        private double dVR = 0;

        private boolean left = false;
        private boolean right = false;

        private boolean atRest = false;
        private BlockBase<T> outer;

        @Override
        public void applyAndReset(double[] state, double[] derivatives) {
            expired = true;
            state[outer.offset + 0] += dX;
            state[outer.offset + 1] += dY;
            state[outer.offset + 2] += dVx;
            state[outer.offset + 3] += dVy;
            state[outer.offset + 5] += dVR;
            dX = 0;
            dY = 0;
            dVx = 0;
            dVy = 0;
            dVR = 0;
            vertices.clear();
            edges.clear();

            if (left && right)
                derivatives[outer.offset + 3] = 0;
            atRest = left && right && state[outer.offset + 2] < 0.5;

            if (left && right) {
                state[outer.offset + 2] *= 0.425;
                state[outer.offset + 3] *= 0.425;
                state[outer.offset + 5] *= 0.425;
            }
            left = false;
            right = false;
        }

        @Override
        public void addImpulse(double J, Vector2D point, Vector2D normal,
                               double distance, boolean rest) {

            Vector2D x = point.subs(center);

            if (rest) {
                double crossProd = normal.X * x.Y - normal.Y * x.X;
                if (crossProd > 0) {
                    left = true;
                } else if (crossProd < 0) {
                    right = true;
                }
            }

            distance = Math.max(0, distance - .1);
            double dX = normal.X * distance * 0.45;
            double dY = normal.Y * distance * 0.45;

            double dVx = J * normal.X / mass;
            double dVy = J * normal.Y / mass;
            double dVR = (-x.X * J * normal.Y + x.Y * J * normal.X)
                    / momentOfInertia;

            setValues(dX, dY, dVx, dVy, dVR);
        }

        private synchronized void setValues(double dX, double dY, double dVx,
                                            double dVy, double dVR) {
            this.dX += dX;
            this.dY += dY;
            this.dVx += dVx;
            this.dVy += dVy;
            this.dVR += dVR;
        }

        @Override
        public boolean atRest() {
            return atRest;
        }

        @Override
        protected void setValues(double[] state, double[] derivatives) {
            center = new Vector2D(state[outer.offset], state[outer.offset + 1]);
            velocity = new Vector2D(state[outer.offset + 2],
                    state[outer.offset + 3]);
            angularVelocity = state[outer.offset + 5];
            double rad = state[outer.offset + 4];
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            double cosWidth = cos * (outer.width / 2d - outer.cornerRadius);
            double cosHeight = cos * (outer.height / 2d - outer.cornerRadius);
            double sinWidth = sin * (outer.width / 2d - outer.cornerRadius);
            double sinHeight = sin * (outer.height / 2d - outer.cornerRadius);

            Vector2D vertex1 = new Vector2D(center.X + cosWidth + sinHeight,
                    center.Y + cosHeight - sinWidth);
            Vector2D vertex2 = new Vector2D(center.X - cosWidth + sinHeight,
                    center.Y + cosHeight + sinWidth);
            Vector2D vertex3 = new Vector2D(center.X - cosWidth - sinHeight,
                    center.Y - cosHeight + sinWidth);
            Vector2D vertex4 = new Vector2D(center.X + cosWidth - sinHeight,
                    center.Y - cosHeight - sinWidth);

            vertices.add(vertex1);
            vertices.add(vertex2);
            vertices.add(vertex3);
            vertices.add(vertex4);

            edges.add(new Vector2D[]{vertex2, vertex1});
            edges.add(new Vector2D[]{vertex3, vertex2});
            edges.add(new Vector2D[]{vertex4, vertex3});
            edges.add(new Vector2D[]{vertex1, vertex4});

        }
    }

    ;

    public interface Vertex {

        public void evaluateImpact(double[] state, double[] derivatives,
                                   List<CollisionMatrix> impacts);

        public CollisionObject getPointSource();

    }

    private static double FRICTION = 0.1;
    private static double GRAVITY = 13;
    protected int offset = 0;
    protected final double width;
    protected final double height;
    private final Vector2D startPosition;
    private final double mass = 1;
    private final double cornerRadius = 1;
    protected final double boundingRadius;
    private final double momentOfInertia = 800;
    protected final CollisionDynamicObjectBase _collisionObject;

    public BlockBase(Vector2D startPosition, double width, double height) {
        this.width = width;
        this.height = height;
        this.startPosition = startPosition;
        this.boundingRadius = 0.5d * Math.sqrt(width * width + height * height);
        _collisionObject = new BlockCollisionObject<T>(this);
    }

    @Override
    public abstract void draw(T graphics, double[] state, double xOffset);

    @Override
    public void evaluateForce(double[] state, double[] outstate) {
        double velocityX = state[offset + 2];
        double velocityY = state[offset + 3];
        double velocityR = state[offset + 5];

        outstate[offset] += velocityX;
        outstate[offset + 1] += velocityY;
        outstate[offset + 2] += -10 * FRICTION * velocityX;
        outstate[offset + 3] += -FRICTION * velocityY;
        outstate[offset + 4] += velocityR;
        outstate[offset + 5] += -FRICTION * 0.5 * velocityR;

    }

    @Override
    public void addExternalForce(double[] state, double[] outstate) {
        outstate[offset + 3] += GRAVITY;
    }

    @Override
    @Deprecated
    public CollisionObject getCollisionObject(double[] state,
                                              double[] derivatives, Vector2D point) {

        Vector2D center = new Vector2D(state[offset], state[offset + 1]);
        Vector2D velocity = new Vector2D(state[offset + 2], state[offset + 3]);
        double angularVelocity = state[offset + 5];
        return new CollisionObject(cornerRadius, mass, point, center, velocity,
                angularVelocity, momentOfInertia,
                getCallback(state, derivatives, point.subs(center)));

    }

    @Override
    public double getDistanceX(double xPosition, double[] state) {
        return Math.abs(state[offset] - xPosition);
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void getPointCollision(double[] state, double[] derivatives,
                                  CollisionObject collisionObject, List<CollisionMatrix> outList) {
        Vector2D center = new Vector2D(state[offset], state[offset + 1]);
        double rad = state[offset + 4];
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double cosWidth = cos * (width / 2d - cornerRadius);
        double cosHeight = cos * (height / 2d - cornerRadius);
        double sinWidth = sin * (width / 2d - cornerRadius);
        double sinHeight = sin * (height / 2d - cornerRadius);

        Vector2D v1 = new Vector2D(center.X + cosWidth + sinHeight,
                center.Y + cosHeight - sinWidth);
        Vector2D v2 = new Vector2D(center.X - cosWidth + sinHeight,
                center.Y + cosHeight + sinWidth);
        Vector2D v3 = new Vector2D(center.X - cosWidth - sinHeight,
                center.Y - cosHeight + sinWidth);
        Vector2D v4 = new Vector2D(center.X + cosWidth - sinHeight,
                center.Y - cosHeight - sinWidth);

        CollisionMatrix collisionMatrix = null;

        collisionMatrix = getEdgeCollision(v2, v1, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
        }
        collisionMatrix = getEdgeCollision(v3, v2, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
        }
        collisionMatrix = getEdgeCollision(v4, v3, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
        }
        collisionMatrix = getEdgeCollision(v1, v4, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
        }

        if (collisionObject.type == Type.STATIC)
            return;

        collisionMatrix = getVertexCollision(v1, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
            return;
        }

        collisionMatrix = getVertexCollision(v2, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
            return;
        }

        collisionMatrix = getVertexCollision(v3, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
            return;
        }

        collisionMatrix = getVertexCollision(v4, collisionObject, state,
                derivatives);
        if (collisionMatrix != null) {
            outList.add(collisionMatrix);
            return;
        }

    }

    @Override
    public DynamicTypes getType() {
        return DynamicTypes.Block;
    }

    public List<CollisionObject> getVertexList(double[] state,
                                               double[] derivatives) {
        Vector2D center = new Vector2D(state[offset], state[offset + 1]);
        double velocityX = state[offset + 2];
        double velocityY = state[offset + 3];
        double velocityR = state[offset + 5];
        double rad = state[offset + 4];
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double cosWidth = cos * (width / 2d - cornerRadius);
        double cosHeight = cos * (height / 2d - cornerRadius);
        double sinWidth = sin * (width / 2d - cornerRadius);
        double sinHeight = sin * (height / 2d - cornerRadius);

        List<CollisionObject> vertexList = new LinkedList<CollisionObject>();
        vertexList.add(getVertex(
                new Vector2D(cosWidth + sinHeight, cosHeight - sinWidth),
                center, velocityX, velocityY, velocityR, state, derivatives));
        vertexList.add(getVertex(
                new Vector2D(-cosWidth + sinHeight, cosHeight + sinWidth),
                center, velocityX, velocityY, velocityR, state, derivatives));
        vertexList.add(getVertex(
                new Vector2D(cosWidth - sinHeight, -cosHeight - sinWidth),
                center, velocityX, velocityY, velocityR, state, derivatives));
        vertexList.add(getVertex(
                new Vector2D(-cosWidth - sinHeight, -cosHeight + sinWidth),
                center, velocityX, velocityY, velocityR, state, derivatives));
        return vertexList;
    }

    @Override
    public double[] initialState() {
        return new double[]{startPosition.X, startPosition.Y, 0, 0,
                -3.159 / 2, 0};
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    protected int mode = 0;

    private ICallbackFunction getCallback(final double[] state,
                                          final double[] derivatives, final Vector2D x) {
        return new ICallbackFunction() {

            @Override
            public void callbackFunctionForce(Vector2D force) {
                derivatives[offset + 2] += 1 * force.X / mass
                        - 1 * state[offset + 2];
                derivatives[offset + 3] += 1 * force.Y / mass
                        - 1 * state[offset + 3];
                derivatives[offset + 5] += (x.Y * force.X - x.X * force.Y)
                        / (momentOfInertia);
            }

            @Override
            public void callbackFunctionImpulse(double distance,
                                                Vector2D normal, double J) {

                if (Math.abs(J) < 1) {

                    state[offset + 2] *= 0.81;
                    state[offset + 3] *= 0.97;
                }

                state[offset] += normal.X * distance * 0.65;
                state[offset + 1] += normal.Y * distance * 0.65;
                // state[offset + 4] += mass * distance * 0.65
                // * (x.Y * normal.X - x.X * normal.Y) / (momentOfInertia);

                state[offset + 2] += J * normal.X / mass;
                state[offset + 3] += J * normal.Y / mass;
                state[offset + 5] += (-x.X * J * normal.Y + x.Y * J * normal.X)
                        / momentOfInertia;

            }

            @Override
            public double callbackImpulseModifier(Vector2D normal) {
                throw new RuntimeException("Method not implmented");
            }

        };
    }

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
        if (y < -maxPenetrationDepth
                || y > collisionObject.radius + cornerRadius)
            return null;

        Vector2D point = A.add(AB.scale(-x));
        CollisionObject thisObject = getCollisionObject(state, derivatives,
                point);
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
        return new CollisionObject(cornerRadius, mass, vertex.add(center),
                center, new Vector2D(velocityX, velocityY), velocityR,
                momentOfInertia, getCallback(state, derivatives, vertex));
    }

    private CollisionMatrix getVertexCollision(Vector2D A,
                                               CollisionObject collisionObject, double[] state,
                                               double[] derivatives) {
        double distance = A.distance(collisionObject.point);
        if (distance > cornerRadius + collisionObject.radius)
            return null;

        Vector2D point = A;
        CollisionObject thisObject = getCollisionObject(state, derivatives,
                point);
        Vector2D relativeVelocity = collisionObject.type == Type.DYNAMIC
                ? (thisObject.getPointVelocity(true))
                .add(collisionObject.getPointVelocity())
                : thisObject.getPointVelocity(true);

        Vector2D AB = A.subs(collisionObject.point);

        return new CollisionMatrix(collisionObject, thisObject,
                AB.scale(-1 / distance), relativeVelocity, null,
                cornerRadius + collisionObject.radius - distance);

    }

    @Override
    public Circle getBoundingCircle(double[] state) {
        return new Circle(new Vector2D(state[offset], state[offset + 1]),
                boundingRadius);
    }

    @Override
    public Category getCategory() {
        return Category.Dynamic;
    }

    public CollisionDynamicObjectBase getCollisionObject(double[] state,
                                                         double[] derivatives) {
        return _collisionObject.get(state, derivatives);
    }

}
