package CollisionHandling;

import java.util.LinkedList;
import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObjectBase.CollisionPoint;
import Auxiliary.CollisionDynamicObjectBase;
import Auxiliary.ICallbackFunction.CallbackContainer;
import Auxiliary.IWorldDynamicObject;
import WorldObjects.BlockBase;
import WorldObjects.CylinderBase;

public class DynamicDynamicCollisions<T> extends CollisionsBase {

    public void handleDynamicDynamicCollision(double[] state,
                                              double[] derivatives, IWorldDynamicObject<T> objectA,
                                              IWorldDynamicObject<T> objectB,
                                              List<CallbackContainer> callbackList) {
        switch (objectA.getType()) {
            case Cylinder:
                switch (objectB.getType()) {
                    case Cylinder:
                        CylinderBase cylinderA = (CylinderBase) objectA;
                        CollisionObject collisionObject = cylinderA
                                .getCollisionObject(state, derivatives, null);
                        LinkedList<CollisionMatrix> outList = new LinkedList<CollisionMatrix>();
                        objectB.getPointCollision(state, derivatives, collisionObject,
                                outList);
                        // if (!outList.isEmpty()) {
                        // CollisionMatrix collisionMatrix = outList.get(0);
                        while (!outList.isEmpty()) {
                            CollisionMatrix collisionMatrix = outList.pop();
                            double J = calculateJ(collisionMatrix);
                            callbackList.add(
                                    new CallbackContainer(1 * collisionMatrix.distance,
                                            collisionMatrix.normal, J,
                                            collisionMatrix.collisionObject1));
                            callbackList.add(
                                    new CallbackContainer(1 * collisionMatrix.distance,
                                            collisionMatrix.normal.scale(-1), J,
                                            collisionMatrix.collisionObject2));
                        }
                        break;
                    case Bridge:
                        // easy !!
                        // check point collision with the slates
                        // check point point collision
                        break;
                    case Block:
                        CollisionMatrix collisionMatrix = getCylinderBlockCollision(
                                (CylinderBase) objectA, (BlockBase) objectB, state,
                                derivatives);
                        if (collisionMatrix != null) {
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal, calculateJ(collisionMatrix),
                                    collisionMatrix.collisionObject1));
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal.scale(-1),
                                    calculateJ(collisionMatrix),
                                    collisionMatrix.collisionObject2));
                        }
                        break;
                }
                break;
            case Bridge:
                switch (objectB.getType()) {
                    case Cylinder:
                        // easy!!
                        break;
                    case Bridge:
                        // Nothing to do, should not happen
                        break;
                    case Block:
                        // tough, we need to do it later
                        break;
                }
                break;
            case Block:
                switch (objectB.getType()) {
                    case Cylinder:
                        CollisionMatrix collisionMatrix = getCylinderBlockCollision(
                                (CylinderBase) objectB, (BlockBase) objectA, state,
                                derivatives);
                        if (collisionMatrix != null) {
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal, calculateJ(collisionMatrix),
                                    collisionMatrix.collisionObject1));
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal.scale(-1),
                                    calculateJ(collisionMatrix),
                                    collisionMatrix.collisionObject2));
                        }
                        break;
                    case Bridge:
                        //// tough, we need to do it later
                        break;
                    case Block:
                        BlockBase<T> block1 = (BlockBase<T>) objectA;
                        BlockBase<T> block2 = (BlockBase<T>) objectB;
                        CollisionDynamicObjectBase collisionObject1 = block1
                                .getCollisionObject(state, derivatives);
                        CollisionDynamicObjectBase collisionObject2 = block2
                                .getCollisionObject(state, derivatives);
                        for (CollisionPoint collisionPoint : collisionObject1
                                .getCollisionPoints(collisionObject2)) {
                            collisionObject1.addImpulse(collisionPoint.J,
                                    collisionPoint.point, collisionPoint.normal,
                                    collisionPoint.distance, collisionObject2.atRest());
                            collisionObject2.addImpulse(collisionPoint.J,
                                    collisionPoint.point,
                                    collisionPoint.normal.scale(-1),
                                    collisionPoint.distance, collisionObject1.atRest());
                        }
                        break;
                }
                break;
            case Pendulum:
                switch (objectB.getType()) {
                    case Cylinder:
                        List<CollisionMatrix> outList = new LinkedList<CollisionMatrix>();
                        objectB.getPointCollision(state, derivatives,
                                objectA.getCollisionObject(state, derivatives, null),
                                outList);
                        for (CollisionMatrix collisionMatrix : outList) {
                            double J = calculateJForConstraintCollision(collisionMatrix,
                                    collisionMatrix.collisionObject1,
                                    collisionMatrix.collisionObject2);
                            callbackList.add(new CallbackContainer(
                                    0.25 * collisionMatrix.distance,
                                    collisionMatrix.normal, J,
                                    collisionMatrix.collisionObject1));
                            callbackList.add(new CallbackContainer(
                                    0.25 * collisionMatrix.distance,
                                    collisionMatrix.normal.scale(-1), J,
                                    collisionMatrix.collisionObject2));
                        }
                        break;
                }
                break;
        }

    }

    private CollisionMatrix getCylinderBlockCollision(CylinderBase<T> cylinder,
                                                      BlockBase<T> block, double[] state, double[] derivatives) {
        List<CollisionMatrix> outList = new LinkedList<CollisionMatrix>();
        CollisionObject collisionObject = cylinder.getCollisionObject(state,
                derivatives, null);
        block.getPointCollision(state, derivatives, collisionObject, outList);
        return outList.isEmpty() ? null : outList.get(0);
    }

}
