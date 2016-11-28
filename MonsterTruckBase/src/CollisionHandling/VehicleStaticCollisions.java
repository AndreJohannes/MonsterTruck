package CollisionHandling;

import java.util.List;

import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.IVehicleObject;
import Auxiliary.IWorldStaticObject;
import Auxiliary.ICallbackFunction.CallbackContainer;
import VehicleObjects.Bumper;
import VehicleObjects.Carbody;

public class VehicleStaticCollisions<T> extends CollisionsBase {

    public void handleVehicleStaticCollision(double[] state,
                                             double[] derivatives, IVehicleObject vehiclePart,
                                             IWorldStaticObject<T> staticObject,
                                             List<CallbackContainer> callbackList) {
        switch (vehiclePart.getType()) {
            case FrontBumper:
            case RearBumper:
                switch (staticObject.getType()) {
                    case Bar:
                        for (CollisionObject object : ((Bumper<T>) vehiclePart)
                                .getVertexList(state, derivatives)) {
                            CollisionMatrix collisionMatrix = staticObject
                                    .getPointCollision(object);
                            if (collisionMatrix != null) {
                                double J = calculateJForStaticCollision(collisionMatrix,
                                        object);
                                callbackList.add(new CallbackContainer(
                                        0.5 * collisionMatrix.distance,
                                        collisionMatrix.normal, J,
                                        object));

                            }
                        }
                        break;
                    case Arc:
                        for (CollisionObject object : ((Bumper<T>) vehiclePart)
                                .getVertexList(state, derivatives)) {
                            CollisionMatrix collisionMatrix = staticObject
                                    .getPointCollision(object);
                            if (collisionMatrix != null) {
                                double J = calculateJForStaticCollision(collisionMatrix,
                                        object);
                                callbackList.add(new CallbackContainer(
                                        0.5 * collisionMatrix.distance,
                                        collisionMatrix.normal, J,
                                        object));
                            }
                        }
                        break;
                }
                break;
            case Bodywork:
                switch (staticObject.getType()) {
                    case Bar:
                        for (CollisionObject object : ((Carbody<T>) vehiclePart)
                                .getVertexList(state, derivatives)) {
                            CollisionMatrix collisionMatrix = staticObject
                                    .getPointCollision(object);
                            if (collisionMatrix != null) {
                                double J = calculateJForStaticCollision(collisionMatrix,
                                        object);
                                callbackList.add(new CallbackContainer(
                                        0.5 * collisionMatrix.distance,
                                        collisionMatrix.normal, J,
                                        object));
                            }
                        }
                        break;
                }
                break;
        }
    }

}
