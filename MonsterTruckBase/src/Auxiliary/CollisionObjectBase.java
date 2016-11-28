package Auxiliary;

import java.util.LinkedList;
import java.util.List;

public class CollisionObjectBase {

    public static enum Type {
        STATIC, DYNAMIC;
    }

    public static class CollisionPoint {

        public final Vector2D point;
        public final Vector2D normal;
        public final double distance;
        public double J = 0;

        public CollisionPoint(Vector2D point, Vector2D normal,
                              double distance) {
            this.distance = distance;
            this.normal = normal;
            this.point = point;
        }

        public void setJ(double value) {
            J = value;
        }
    }

    protected static CollisionPoint getCollisionPoint(Vector2D vertexObject1,
                                                      Vector2D vertexObject2,
                                                      double radiusObject1, double radiusObject2,
                                                      boolean reverseNormal) {
        double distance = vertexObject1.distance(vertexObject2);
        if (distance <= radiusObject1 + radiusObject2)
            return new CollisionPoint(
                    vertexObject1.add(vertexObject2).scale(0.5),
                    reverseNormal
                            ? vertexObject2.subs(vertexObject1).normalise()
                            : vertexObject1.subs(vertexObject2).normalise(),
                    radiusObject1 + radiusObject2 - distance);
        return null;
    }

    protected static CollisionPoint getCollisionPoint(Vector2D vertexObject1,
                                                      Vector2D vertexAObject2, Vector2D vertexBObject2,
                                                      double radiusObject1, double radiusObject2, boolean reverseNormal) {
        Vector2D AB = vertexAObject2.subs(vertexBObject2);
        Vector2D AC = vertexObject1.subs(vertexAObject2);
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
        if (y < -maxPenetrationDepth || y > radiusObject1 + radiusObject2)
            return null;

        return new CollisionPoint(vertexObject1,
                reverseNormal ? normal.scale(-1) : normal,
                radiusObject1 + radiusObject2 - y);
    }

    protected static void getCollisionPoints(CollisionObjectBase object1,
                                             CollisionObjectBase object2,
                                             LinkedList<CollisionPoint> list,
                                             boolean reverseNormal) {

        for (Vector2D point : object1.vertices) {
            for (Vector2D[] edge : object2.edges) {
                CollisionPoint normalAndDistance = getCollisionPoint(point,
                        edge[0], edge[1], object1.radius, object2.radius,
                        reverseNormal);
                if (normalAndDistance != null)
                    list.add(normalAndDistance);
            }
        }
        for (Vector2D point : object2.vertices) {
            for (Vector2D[] edge : object1.edges) {
                CollisionPoint normalAndDistance = getCollisionPoint(point,
                        edge[0], edge[1], object2.radius, object1.radius,
                        !reverseNormal);
                if (normalAndDistance != null)
                    list.add(normalAndDistance);
            }
        }
        for (Vector2D point1 : object1.vertices) {
            for (Vector2D point2 : object2.vertices) {
                CollisionPoint normalAndDistance = getCollisionPoint(point1,
                        point2, object1.radius, object2.radius, reverseNormal);
                if (normalAndDistance != null)
                    list.add(normalAndDistance);
            }
        }
    }

    protected static double calculateJ(CollisionObjectBase object1,
                                       CollisionObjectBase object2, CollisionPoint collisionPoint) {
        if (object1.type == Type.DYNAMIC && object2.type == Type.DYNAMIC) {
            return CollisionDynamicObjectBase.calculateJ(
                    (CollisionDynamicObjectBase) object1,
                    (CollisionDynamicObjectBase) object2, collisionPoint);
        }
        if (object1.type == Type.DYNAMIC) {
            return CollisionDynamicObjectBase.calculateJ(object2,
                    (CollisionDynamicObjectBase) object1, collisionPoint,
                    false);
        }
        if (object2.type == Type.DYNAMIC) {
            return CollisionDynamicObjectBase.calculateJ(object1,
                    (CollisionDynamicObjectBase) object2, collisionPoint, true);
        }
        throw new RuntimeException("How did you get here!?");
    }

    protected static double calculateJ(CollisionDynamicObjectBase object1,
                                       CollisionDynamicObjectBase object2, CollisionPoint collisionPoint) {
        Vector2D normal = collisionPoint.normal;
        Vector2D velocity = object1.getPointVelocity(collisionPoint.point)
                .subs(object2.getPointVelocity(collisionPoint.point));
        Vector2D vec1 = collisionPoint.point.subs(object1.center);
        double momentOfInertia1 = object1.momentOfInertia;
        double mass1 = object1.mass;
        Vector2D vec2 = collisionPoint.point.subs(object2.center);
        double momentOfInertia2 = object2.momentOfInertia;
        double mass2 = object2.mass;
        double dot = normal.dot(velocity);
        if (dot > 0)
            return 0;
        return -1.1 * dot
                / (1 / mass1 + 1 / mass2
                + Math.pow(normal.X * vec1.Y - normal.Y * vec1.X, 2)
                / momentOfInertia1
                + Math.pow(normal.X * vec2.Y - normal.Y * vec2.X, 2)
                / momentOfInertia2);
    }

    protected static double calculateJ(CollisionObjectBase object1,
                                       CollisionDynamicObjectBase object2, CollisionPoint collisionPoint,
                                       boolean revertVel) {
        Vector2D normal = collisionPoint.normal;
        Vector2D velocity = object2.getPointVelocity(collisionPoint.point)
                .scale(revertVel ? -1 : 1);
        Vector2D vec = collisionPoint.point.subs(object2.center);
        double momentOfInertia = object2.momentOfInertia;
        double mass = object2.mass;
        double dot = normal.X * velocity.X + normal.Y * velocity.Y;
        if (dot > 0)
            return 0;
        return momentOfInertia != Double.POSITIVE_INFINITY
                ? -1.1 * dot * momentOfInertia * mass
                / (momentOfInertia + mass * Math
                .pow(normal.X * vec.Y - normal.Y * vec.X, 2))
                : -1.5 * dot * mass;
    }

    protected final double mass;
    protected final double momentOfInertia;
    protected Vector2D velocity;
    protected double angularVelocity;
    protected final double radius;
    protected final List<Vector2D> vertices;
    protected final List<Vector2D[]> edges;
    protected final boolean isOverruling;
    protected final Type type;

    public CollisionObjectBase(double mass, double momentOfInertia,
                               double radius, List<Vector2D> vertices, List<Vector2D[]> edges,
                               boolean isOverruling, Type type) {
        this.mass = mass;
        this.momentOfInertia = momentOfInertia;
        this.radius = radius;
        this.vertices = vertices;
        this.edges = edges;
        this.isOverruling = isOverruling;
        this.type = type;
    }

    public List<CollisionPoint> getCollisionPoints(CollisionObjectBase object) {
        return this.getCollisionPoints(object, false);
    }

    protected List<CollisionPoint> getCollisionPoints(
            CollisionObjectBase object, boolean reverseNormal) {
        // TODO: do we have to create that list every time ??
        LinkedList<CollisionPoint> collisionList = new LinkedList<CollisionPoint>();
        if (object.isOverruling) {
            if (this.isOverruling)
                throw new RuntimeException(
                        "Collision of two overruling objects is currently not allowed");
            return object.getCollisionPoints(this, !reverseNormal);
        }

        CollisionDynamicObjectBase.getCollisionPoints(this, object,
                collisionList, reverseNormal);
        for (CollisionPoint collisionPoint : collisionList) {
            double J = reverseNormal ? calculateJ(object, this, collisionPoint)
                    : calculateJ(this, object, collisionPoint);
            collisionPoint.setJ(J);
        }
        return collisionList;
    }

    protected final List<Vector2D> getVertices(CollisionObjectBase object) {
        return object.vertices;
    }

    protected final List<Vector2D[]> getEdges(CollisionObjectBase object) {
        return object.edges;
    }

    protected final double getRadius(CollisionObjectBase object) {
        return object.radius;
    }

}
