package CollisionHandling;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObject.Type;
import Auxiliary.Vector2D;

public class CollisionsBase {

    protected double calculateJ(CollisionMatrix collisionMatrix) {
	if (collisionMatrix.collisionObject1.type == Type.STATIC)
	    return calculateJForStaticCollision(collisionMatrix,
		    collisionMatrix.collisionObject2);
	if (collisionMatrix.collisionObject2.type == Type.STATIC)
	    return calculateJForStaticCollision(collisionMatrix,
		    collisionMatrix.collisionObject1);

	return calculateJForDynamicCollision(collisionMatrix);

    }

    protected double calculateJForStaticCollision(
	    CollisionMatrix collisionMatrix, CollisionObject dynamicObject) {
	Vector2D normal = collisionMatrix.normal;
	Vector2D velocity = collisionMatrix.relativeVelocity;
	Vector2D vec = dynamicObject.point.subs(dynamicObject.center);
	double momentOfInertia = dynamicObject.momentumOfInertia;
	double mass = dynamicObject.mass;
	double dot = normal.X * velocity.X + normal.Y * velocity.Y;
	if (dot > 0)
	    return 0;
	return momentOfInertia != Double.POSITIVE_INFINITY
		? -1.1 * dot * momentOfInertia * mass
			/ (momentOfInertia + mass * Math
				.pow(normal.X * vec.Y - normal.Y * vec.X, 2))
		: -1.5 * dot * mass;
    }

    protected double calculateJForDynamicCollision(
	    CollisionMatrix collisionMatrix) {
	Vector2D normal = collisionMatrix.normal;
	Vector2D velocity = collisionMatrix.relativeVelocity;
	Vector2D vec1 = collisionMatrix.collisionObject1.point
		.subs(collisionMatrix.collisionObject1.center);
	double momentOfInertia1 = collisionMatrix.collisionObject1.momentumOfInertia;
	double mass1 = collisionMatrix.collisionObject1.mass;
	Vector2D vec2 = collisionMatrix.collisionObject2.point
		.subs(collisionMatrix.collisionObject2.center);
	double momentOfInertia2 = collisionMatrix.collisionObject2.momentumOfInertia;
	double mass2 = collisionMatrix.collisionObject2.mass;
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

    protected double calculateJForConstraintCollision(
	    CollisionMatrix collisionMatrix, CollisionObject constraintObject,
	    CollisionObject dynamicObject) {
	Vector2D normal = collisionMatrix.normal;
	Vector2D velocity = collisionMatrix.relativeVelocity;
	double momentOfInertia1 = constraintObject.momentumOfInertia;
	if (momentOfInertia1 != Double.POSITIVE_INFINITY)
	    throw new RuntimeException(
		    "Finite moments of inertia currently not supported");
	double mass1 = constraintObject.mass;
	Vector2D vec2 = dynamicObject.point.subs(dynamicObject.center);
	double momentOfInertia2 = dynamicObject.momentumOfInertia;
	double mass2 = dynamicObject.mass;
	double dot = normal.dot(velocity);
	if (dot > 0)
	    return 0;
	double constraintFactor = constraintObject.callbackFunction
		.callbackImpulseModifier(normal);
	return -dot / (constraintFactor + 1 / mass2
		+ Math.pow(normal.X * vec2.Y - normal.Y * vec2.X, 2)
			/ momentOfInertia2);
    }

}
