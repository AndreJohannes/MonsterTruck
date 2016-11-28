package CollisionHandling;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.SynchronousQueue;

import Auxiliary.Graph;
import Auxiliary.ICallbackFunction.CallbackContainer;
import Auxiliary.IVehicleObject;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.IWorldObjectBase;
import Auxiliary.IWorldStaticObject;

public class CollisionHandler<T> {

    private class CollisionHandlerThread implements Runnable {

	@Override
	public void run() {
	    IWorldObjectBase<T> objectA = null;
	    try {
		while ((objectA = inputQueue.take()) != null) {
		    for (IWorldObjectBase<T> objectB : graph
			    .getAdjacentVertexIterable(objectA)) {
			handleCollision(state, derivatives, objectA, objectB,
				callbackList);
		    }
		    phaser.arriveAndDeregister();
		}
	    } catch (InterruptedException e) {
		e.printStackTrace();
		System.exit(1);
	    }
	}

    }

    private static final LinkedBlockingQueue<IWorldObjectBase> inputQueue = new LinkedBlockingQueue<>();
    // I am sure following fields do not need to be made thread safe via
    // an atomic reference.
    private Graph<IWorldObjectBase<T>> graph;
    private Phaser phaser = null;
    private double[] state;
    private double[] derivatives;

    private List<CallbackContainer> callbackList;

    private DynamicStaticCollisions<T> dynamicStaticCollisionHandler = new DynamicStaticCollisions<T>();
    private DynamicDynamicCollisions<T> dynamicDynamicCollisionHandler = new DynamicDynamicCollisions<T>();
    private VehicleStaticCollisions<T> vehicleStaticCollisionHandler = new VehicleStaticCollisions<T>();
    private VehicleDynamicCollisions<T> vehicleDynamicCollisionHandler = new VehicleDynamicCollisions<T>();

    public CollisionHandler() {
	// Open three threads
	(new Thread(new CollisionHandlerThread(), "CllHdl 1")).start();
	(new Thread(new CollisionHandlerThread(), "CllHdl 2")).start();
	(new Thread(new CollisionHandlerThread(), "CllHdl 2")).start();
    }

    public void arriveAndAwait() {
	phaser.arriveAndAwaitAdvance();
	phaser = null;
    }

    public void handleCollision(double[] state, double[] derivatives,
	    IVehicleObject vehicle, IWorldDynamicObject<T> dynamicObject,
	    List<CallbackContainer> callbackList) {
	vehicleDynamicCollisionHandler.handleVehicleDynamicCollision(state,
		derivatives, vehicle, dynamicObject, callbackList);
    }

    public void handleCollision(double[] state, double[] derivatives,
	    IVehicleObject vehiclePart, IWorldStaticObject<T> staticObject,
	    List<CallbackContainer> callbackList) {
	vehicleStaticCollisionHandler.handleVehicleStaticCollision(state,
		derivatives, vehiclePart, staticObject, callbackList);
    }

    public void offer(IWorldObjectBase<T> object) {
	phaser.register();
	inputQueue.offer(object);
    }

    public void register(double[] state, double[] derivatives,
	    Graph<IWorldObjectBase<T>> graph,
	    List<CallbackContainer> callbackList) {
	if (phaser != null)
	    throw new RuntimeException("Something wrong with the sync");
	phaser = new Phaser();
	phaser.register();
	this.state = state;
	this.derivatives = derivatives;
	this.graph = graph;
	this.callbackList = callbackList;
    }

    private void handleCollision(double[] state, double[] derivatives,
	    IWorldObjectBase objectA, IWorldObjectBase objectB,
	    List<CallbackContainer> callbackList) {
	switch (objectA.getCategory()) {
	case Dynamic:
	    switch (objectB.getCategory()) {
	    case Dynamic:
		dynamicDynamicCollisionHandler.handleDynamicDynamicCollision(
			state, derivatives, (IWorldDynamicObject<T>) objectA,
			(IWorldDynamicObject<T>) objectB, callbackList);
		break;
	    case Static:
		dynamicStaticCollisionHandler.handleDynamicStaticCollision(
			state, derivatives, (IWorldDynamicObject<T>) objectA,
			(IWorldStaticObject<T>) objectB, callbackList);
		break;
	    case Vehicle:
		vehicleDynamicCollisionHandler.handleVehicleDynamicCollision(
			state, derivatives, (IVehicleObject) objectB,
			(IWorldDynamicObject<T>) objectA, callbackList);
	    default:
		throw new RuntimeException("Should not get here");
	    }
	    break;
	case Static:
	    switch (objectB.getCategory()) {
	    case Dynamic:
		dynamicStaticCollisionHandler.handleDynamicStaticCollision(
			state, derivatives, (IWorldDynamicObject<T>) objectB,
			(IWorldStaticObject<T>) objectA, callbackList);
		break;
	    case Vehicle:
		vehicleStaticCollisionHandler.handleVehicleStaticCollision(
			state, derivatives, (IVehicleObject) objectB,
			(IWorldStaticObject<T>) objectA, callbackList);
	    default:
		throw new RuntimeException("Should not get here");
	    }
	    break;
	case Vehicle:
	    switch (objectB.getCategory()) {
	    case Dynamic:
		vehicleDynamicCollisionHandler.handleVehicleDynamicCollision(
			state, derivatives, (IVehicleObject) objectA,
			(IWorldDynamicObject<T>) objectB, callbackList);
		break;
	    case Static:
		vehicleStaticCollisionHandler.handleVehicleStaticCollision(
			state, derivatives, (IVehicleObject) objectA,
			(IWorldStaticObject<T>) objectB, callbackList);
		break;
	    default:
		throw new RuntimeException("Should not get here");
	    }
	    break;
	default:
	    throw new RuntimeException("Should not get here");
	}
    }

}
