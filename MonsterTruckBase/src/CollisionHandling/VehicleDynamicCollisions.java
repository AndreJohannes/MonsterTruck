package CollisionHandling;

import java.util.LinkedList;
import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.IVehicleObject;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.IWorldStaticObject;
import VehicleObjects.Bumper;
import VehicleObjects.Carbody;
import Auxiliary.ICallbackFunction.CallbackContainer;

public class VehicleDynamicCollisions<T> extends CollisionsBase {

    public void handleVehicleDynamicCollision(double[] state,
                                              double[] derivatives, IVehicleObject vehiclePart,
                                              IWorldDynamicObject<T> dynamicObject,
                                              List<CallbackContainer> callbackList) {
        switch (vehiclePart.getType()) {
            case FrontBumper:
            case RearBumper:
                switch (dynamicObject.getType()) {
                    case Cylinder:
                        CollisionObject collisionCylinder = dynamicObject
                                .getCollisionObject(state, derivatives, null);
                        List<CollisionMatrix> outList = new LinkedList();
                        vehiclePart.getPointCollision(state, derivatives,
                                collisionCylinder, outList);
                        for (CollisionMatrix collisionMatrix : outList) {
                            double j = calculateJForDynamicCollision(collisionMatrix);
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal, j,
                                    collisionMatrix.collisionObject1));
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal.scale(-1), j,
                                    collisionMatrix.collisionObject2));
                        }
                        break;
                    case Block:
                        for (CollisionObject object : ((Bumper<T>) vehiclePart)
                                .getVertexList(state, derivatives)) {
                            outList = new LinkedList();
                            dynamicObject.getPointCollision(state, derivatives, object,
                                    outList);
                            for (CollisionMatrix collisionMatrix : outList) {
                                double j = calculateJForDynamicCollision(
                                        collisionMatrix);
                                callbackList.add(new CallbackContainer(
                                        0.5 * collisionMatrix.distance,
                                        collisionMatrix.normal, j,
                                        collisionMatrix.collisionObject1));
                                callbackList.add(new CallbackContainer(
                                        0.5 * collisionMatrix.distance,
                                        collisionMatrix.normal.scale(-1), j,
                                        collisionMatrix.collisionObject2));
                            }
                        }
                        break;
                    case Pendulum:
                        CollisionObject collisionPendulum = dynamicObject
                                .getCollisionObject(state, derivatives, null);
                        outList = new LinkedList();
                        vehiclePart.getPointCollision(state, derivatives,
                                collisionPendulum, outList);
                        for (CollisionMatrix collisionMatrix : outList) {
                            double j = calculateJForConstraintCollision(collisionMatrix,
                                    collisionPendulum,
                                    collisionMatrix.collisionObject2);
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal, j,
                                    collisionMatrix.collisionObject1));
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal.scale(-1), j,
                                    collisionMatrix.collisionObject2));
                        }
                        break;
                    case Bridge:
                        for (CollisionObject object : ((Bumper<T>) vehiclePart)
                                .getVertexList(state, derivatives)) {
                            outList = new LinkedList();
                            dynamicObject.getPointCollision(state, derivatives, object,
                                    outList);
                            for (CollisionMatrix collisionMatrix : outList) {
                                double j = calculateJForStaticCollision(collisionMatrix,
                                        collisionMatrix.collisionObject1);
                                callbackList.add(new CallbackContainer(
                                        0.5 * collisionMatrix.distance,
                                        collisionMatrix.normal, j,
                                        collisionMatrix.collisionObject1));
                                // callbackList.add(new CallbackContainer(
                                // 0.5 * collisionMatrix.distance,
                                // collisionMatrix.normal.scale(-1), j,
                                // collisionMatrix.collisionObject2.callbackFunction));
                            }
                        }
                        break;
                    case DoublePendulum:
                        CollisionObject object = dynamicObject.getCollisionObject(state,
                                derivatives, null);
                        outList = new LinkedList();
                        vehiclePart.getPointCollision(state, derivatives, object,
                                outList);
                        for (CollisionMatrix collisionMatrix : outList) {
                            double j = calculateJForConstraintCollision(collisionMatrix,
                                    object, collisionMatrix.collisionObject2);
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal, j,
                                    collisionMatrix.collisionObject1));
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal.scale(-1), j,
                                    collisionMatrix.collisionObject2));
                        }
                        break;
                }
                break;
            case Bodywork:
                switch (dynamicObject.getType()) {
                    // case Cylinder:
                    // CollisionObject collisionCylinder = dynamicObject
                    // .getCollisionObject(state, derivatives, null);
                    // List<CollisionMatrix> outList = new LinkedList();
                    // bodywork.getPointCollision(state, derivatives, collisionCylinder,
                    // outList);
                    // for (CollisionMatrix collisionMatrix : outList) {
                    // double j = calculateJForDynamicCollision(collisionMatrix);
                    // callbackList.add(new CallbackContainer(
                    // 0.5 * collisionMatrix.distance, collisionMatrix.normal,
                    // j, collisionMatrix.collisionObject1.callbackFunction));
                    // callbackList.add(new CallbackContainer(
                    // 0.5 * collisionMatrix.distance,
                    // collisionMatrix.normal.scale(-1), j,
                    // collisionMatrix.collisionObject2.callbackFunction));
                    // }
                    // break;
                    // case Block:
                    // for (CollisionObject object : bodywork.getVertexList(state,
                    // derivatives)) {
                    // outList = new LinkedList();
                    // dynamicObject.getPointCollision(state, derivatives, object,
                    // outList);
                    // for (CollisionMatrix collisionMatrix : outList) {
                    // double j = calculateJForDynamicCollision(collisionMatrix);
                    // callbackList.add(new CallbackContainer(
                    // 0.5 * collisionMatrix.distance,
                    // collisionMatrix.normal, j,
                    // collisionMatrix.collisionObject1.callbackFunction));
                    // callbackList.add(new CallbackContainer(
                    // 0.5 * collisionMatrix.distance,
                    // collisionMatrix.normal.scale(-1), j,
                    // collisionMatrix.collisionObject2.callbackFunction));
                    // }
                    // }
                    // break;
                    // case Pendulum:
                    // CollisionObject collisionPendulum = dynamicObject
                    // .getCollisionObject(state, derivatives, null);
                    // outList = new LinkedList();
                    // bodywork.getPointCollision(state, derivatives, collisionPendulum,
                    // outList);
                    // for (CollisionMatrix collisionMatrix : outList) {
                    // double j = calculateJForConstraintCollision(collisionMatrix,
                    // collisionPendulum, collisionMatrix.collisionObject2);
                    // callbackList.add(new CallbackContainer(
                    // 0.5 * collisionMatrix.distance, collisionMatrix.normal,
                    // j, collisionMatrix.collisionObject1.callbackFunction));
                    // callbackList.add(new CallbackContainer(
                    // 0.5 * collisionMatrix.distance,
                    // collisionMatrix.normal.scale(-1), j,
                    // collisionMatrix.collisionObject2.callbackFunction));
                    // }
                    // break;
                    case Bridge:
                        for (CollisionObject object : ((Carbody<T>) vehiclePart)
                                .getVertexList(state, derivatives)) {
                            List<CollisionMatrix> outList = new LinkedList();
                            dynamicObject.getPointCollision(state, derivatives, object,
                                    outList);
                            for (CollisionMatrix collisionMatrix : outList) {
                                double j = calculateJForStaticCollision(collisionMatrix,
                                        collisionMatrix.collisionObject1);
                                callbackList.add(new CallbackContainer(
                                        0.5 * collisionMatrix.distance,
                                        collisionMatrix.normal, j,
                                        collisionMatrix.collisionObject1));
                                // callbackList.add(new CallbackContainer(
                                // 0.5 * collisionMatrix.distance,
                                // collisionMatrix.normal.scale(-1), j,
                                // collisionMatrix.collisionObject2.callbackFunction));
                            }
                        }
                        break;
                    case DoublePendulum:
                        CollisionObject object = dynamicObject.getCollisionObject(state,
                                derivatives, null);
                        List<CollisionMatrix> outList = new LinkedList();
                        vehiclePart.getPointCollision(state, derivatives, object, outList);
                        for (CollisionMatrix collisionMatrix : outList) {
                            double j = calculateJForConstraintCollision(collisionMatrix,
                                    object, collisionMatrix.collisionObject2);
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal, j,
                                    collisionMatrix.collisionObject1));
                            callbackList.add(new CallbackContainer(
                                    0.5 * collisionMatrix.distance,
                                    collisionMatrix.normal.scale(1), j, // TODO; why
                                    // is the
                                    // scale not
                                    // -1?
                                    collisionMatrix.collisionObject2));
                        }
                        break;
                }
                break;
        }
    }

}
