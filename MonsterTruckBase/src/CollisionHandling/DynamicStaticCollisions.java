package CollisionHandling;

import java.util.List;

import Auxiliary.CollisionDynamicObjectBase;
import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.CollisionObjectBase;
import Auxiliary.CollisionObjectBase.CollisionPoint;
import Auxiliary.ICallbackFunction.CallbackContainer;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.IWorldStaticObject;
import WorldObjects.BlockBase;
import WorldObjects.CylinderBase;

public class DynamicStaticCollisions<T> extends CollisionsBase {

    public void handleDynamicStaticCollision(double[] state,
                                             double[] derivatives, IWorldDynamicObject<T> dynamicObject,
                                             IWorldStaticObject<T> staticObject,
                                             List<CallbackContainer> callbackList) {
        switch (dynamicObject.getType()) {
            case Cylinder:
                CylinderBase<T> cylinder = (CylinderBase<T>) dynamicObject;
                CollisionObject collisionObject = cylinder.getCollisionObject(state,
                        derivatives, null);
                CollisionMatrix collisionMatrix = staticObject
                        .getPointCollision(collisionObject);
                if (collisionMatrix != null) {
                    callbackList.add(new CallbackContainer(collisionMatrix.distance,
                            collisionMatrix.normal, calculateJ(collisionMatrix),
                            collisionMatrix.collisionObject1));
                }
                break;
            case Bridge:
                // we do not need to do anything,, should not happen
                break;
            case Block:
                switch (staticObject.getType()) {
                    case Bar:
                        BlockBase<T> block = (BlockBase<T>) dynamicObject;
                        CollisionDynamicObjectBase collisionObject1 = block
                                .getCollisionObject(state, derivatives);
                        CollisionObjectBase collisionObject2 = staticObject
                                .getCollisionObject();
                        List<CollisionPoint> collisionList = collisionObject1
                                .getCollisionPoints(collisionObject2);
                        for (CollisionPoint collisionPoint : collisionList) {
                            collisionObject1.addImpulse(collisionPoint.J,
                                    collisionPoint.point, collisionPoint.normal,
                                    collisionPoint.distance, true);
                        }
                        break;
                    case Point:
                        block = (BlockBase<T>) dynamicObject;
                        collisionObject1 = block.getCollisionObject(state, derivatives);
                        collisionObject2 = staticObject.getCollisionObject();
                        collisionList = collisionObject1
                                .getCollisionPoints(collisionObject2);
                        for (CollisionPoint collisionPoint : collisionList) {
                            collisionObject1.addImpulse(collisionPoint.J,
                                    collisionPoint.point, collisionPoint.normal,
                                    collisionPoint.distance, true);
                        }
                        break;
                    case Arc:
                        block = (BlockBase<T>) dynamicObject;
                        collisionObject1 = block.getCollisionObject(state, derivatives);
                        collisionObject2 = staticObject.getCollisionObject();
                        collisionList = collisionObject1
                                .getCollisionPoints(collisionObject2);
                        for (CollisionPoint collisionPoint : collisionList) {
                            collisionObject1.addImpulse(collisionPoint.J,
                                    collisionPoint.point, collisionPoint.normal,
                                    collisionPoint.distance, true);
                        }
                        break;
                }
                break;
        }
    }
}