package VehicleObjects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObject.Type;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.VehiclePartTypes;
import Auxiliary.ICallbackFunction;
import Auxiliary.IVehicleObject;
import Auxiliary.Vector2D;
import Auxiliary.Vector2D.Circle;
import VehicleObjects.VehicleBase.Geometry;

public class Carbody<T> implements IVehicleObject<T> {

    private final Geometry geometry;
    private final VehicleBase<T> vehicle;
    private final double boundinRadius;

    public Carbody(Geometry geometry, VehicleBase<T> vehicle) {
        this.geometry = geometry;
        this.vehicle = vehicle;
        this.boundinRadius = getBoundingRadius(geometry);
    }

    @Override
    public Circle getBoundingCircle(double[] state) {
        return new Circle(new Vector2D(state[0], state[1]), boundinRadius);
    }

    @Override
    public CollisionObject getCollisionObject(double[] state,
                                              double[] derivatives, Vector2D point) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public double getDistanceX(double xPosition, double[] state) {
        throw new RuntimeException("Not supported");
    }

    public void getPointCollision(double[] state, double[] derivatives,
                                  CollisionObject collisionObject, List<CollisionMatrix> outList) {

        double rad = state[4];
        double sin = Math.sin(rad), cos = Math.cos(rad);

        Vector2D position = new Vector2D(state[0], state[1]);

        Iterator<Vector2D> iterator = geometry.CMtoBodyGeometry.iterator();
        if (!iterator.hasNext())
            return;
        Vector2D pointA = iterator.next().rotate(sin, cos).add(position);
        while (iterator.hasNext()) {
            Vector2D pointB = iterator.next().rotate(sin, cos).add(position);
            CollisionMatrix collisionMatrix = getEdgeCollision(pointA, pointB,
                    collisionObject, state, derivatives);
            if (collisionMatrix != null)
                outList.add(collisionMatrix);
            pointA = pointB;
        }

    }

    @Override
    public VehiclePartTypes getType() {
        return VehiclePartTypes.Bodywork;
    }

    public List<CollisionObject> getVertexList(double[] state,
                                               double[] derivatives) {
        Vector2D position = new Vector2D(state[0], state[1]);
        double rad = state[4];
        double sin = Math.sin(rad), cos = Math.cos(rad);

        double velocityX = state[2];
        double velocityY = state[3];
        double velocityR = state[5];

        List<CollisionObject> retList = new LinkedList<CollisionObject>();
        for (Vector2D point : geometry.CMtoBodyGeometry) {
            retList.add(getVertex(
                    new Vector2D(cos * point.X + sin * point.Y,
                            cos * point.Y - sin * point.X),
                    position, velocityX, velocityY, velocityR, state,
                    derivatives));
        }
        return retList;
    }

    private ICallbackFunction getCallback(final double[] state,
                                          final double[] derivatives, final Vector2D x) {
        return new ICallbackFunction() {

            @Override
            public void callbackFunctionForce(Vector2D force) {
                // derivatives[offset + 2] += 1 * force.X / mass;
                // derivatives[offset + 3] += 1 * force.Y / mass;
                // derivatives[offset + 5] += 1 * (x.Y * force.X - x.X *
                // force.Y)
                // / (momentOfInertia);
            }

            @Override
            public void callbackFunctionImpulse(double distance,
                                                Vector2D normal, double J) {
                incHealth(-1 * J);
                state[2] += J * normal.X / geometry.mass;
                state[3] += J * normal.Y / geometry.mass;
                state[5] += (-x.X * J * normal.Y + x.Y * J * normal.X)
                        / geometry.momentOfInertia;
                // distance = Math.max(-1, distance);
                // distance = Math.min(1, distance);
                // state[offset] += distance * normal.X;
                // state[offset + 1] += distance * normal.Y;
            }

            @Override
            public double callbackImpulseModifier(Vector2D normal) {
                throw new RuntimeException("Method not implmented");
            }
        };
    }

    // TODO: almost code duplication!!
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
        if (y < -maxPenetrationDepth || y > collisionObject.radius + 1)
            return null;

        Vector2D point = A.add(AB.scale(-x));
        Vector2D center = new Vector2D(state[0], state[1]);
        Vector2D velocity = new Vector2D(state[2], state[3]);
        double angularVelocity = state[5];
        CollisionObject thisObject = new CollisionObject(1, geometry.mass,
                point, center, velocity, angularVelocity,
                geometry.momentOfInertia,
                getCallback(state, derivatives, point.subs(center)));
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
        return new CollisionObject(1, geometry.mass, vertex.add(center), center,
                new Vector2D(velocityX, velocityY), velocityR,
                geometry.momentOfInertia,
                getCallback(state, derivatives, vertex));
    }

    private void incHealth(double value) {
        vehicle.incHealth(value);
    }

    private double getBoundingRadius(Geometry geometry) {
        double radius = Double.MAX_VALUE;
        for (Vector2D vector : geometry.CMtoBodyGeometry) {
            double length = vector.norm();
            radius = radius > length ? length : radius;
        }
        return radius;
    }

    @Override
    public void draw(T graphics, double[] state, double offset) {
        // For now: nothing to be done
    }

    @Override
    public Category getCategory() {
        return Category.Vehicle;
    }

}
