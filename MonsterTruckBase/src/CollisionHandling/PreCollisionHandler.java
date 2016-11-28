package CollisionHandling;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicReference;

import Auxiliary.EWorldObjects.DynamicTypes;
import Auxiliary.Graph;
import Auxiliary.IVehicleObject;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.IWorldObjectBase;
import Auxiliary.IWorldStaticObject;
import Auxiliary.IWorldTokenObject;
import Auxiliary.Vector2D.Circle;

public class PreCollisionHandler<T> implements Runnable {

    private static double MARGIN = 20;
    private static double INNER_DISTANCE_MARGIN = 1000;
    private static double OUTER_DISTANCE_MARGIN = 2000;

    private final SynchronousQueue<double[]> inputQueue;
    private final List<IWorldStaticObject<T>> staticObjectList;
    private final List<IWorldDynamicObject<T>> dynamicObjectList;
    private final List<IVehicleObject<T>> vehicleObjectList;
    private final List<IWorldTokenObject<T>> tokenObjectList;

    private final AtomicReference<Graph<IWorldObjectBase<T>>> graph = new AtomicReference<Graph<IWorldObjectBase<T>>>(
	    new Graph<IWorldObjectBase<T>>());

    private final AtomicReference<List<IWorldObjectBase<T>>> visibleObjects = new AtomicReference<List<IWorldObjectBase<T>>>(
	    new LinkedList<IWorldObjectBase<T>>());

    public PreCollisionHandler(
	    List<IWorldStaticObject<T>> staticObjectInputList,
	    List<IWorldDynamicObject<T>> dynamicObjectInputList,
	    List<IWorldTokenObject<T>> tokenObjectInputList,
	    List<IVehicleObject<T>> vehicleObjectList) {
	this.inputQueue = new SynchronousQueue<>();
	this.staticObjectList = staticObjectInputList;
	this.dynamicObjectList = dynamicObjectInputList;
	this.tokenObjectList = tokenObjectInputList;
	this.vehicleObjectList = vehicleObjectList;
	(new Thread(this,"PreCollHdl")).start();
    }

    @Override
    public void run() {
	double[] state = null;
	try {
	    while ((state = inputQueue.take()) != null) {
		Graph<IWorldObjectBase<T>> graph = new Graph<IWorldObjectBase<T>>();
		List<IWorldObjectBase<T>> visibleObjects = new LinkedList<>();
		LinkedList<IWorldDynamicObject<T>> innerDynamicObjects = new LinkedList<IWorldDynamicObject<T>>();
		LinkedList<IWorldDynamicObject<T>> outerDynamicObjects = new LinkedList<IWorldDynamicObject<T>>();
		LinkedList<IWorldDynamicObject<T>> activeDynamicObjects;
		LinkedList<IWorldStaticObject<T>> innerStaticObjects = new LinkedList<IWorldStaticObject<T>>();
		LinkedList<IWorldStaticObject<T>> outerStaticObjects = new LinkedList<IWorldStaticObject<T>>();

		double xPosition = state[0];// PositionOfCar

		populateDynamicObjectsLists(xPosition, state,
			innerDynamicObjects, outerDynamicObjects);

		populateStaticObjectsLists(xPosition, innerStaticObjects,
			outerStaticObjects);

		activeDynamicObjects = addDynamicDynamicConnections(
			innerDynamicObjects, outerDynamicObjects, graph, state);

		addDynamicStaticConnections(activeDynamicObjects,
			innerStaticObjects, outerStaticObjects, graph, state);

		addVehicleStaticConnections(vehicleObjectList,
			innerStaticObjects, graph, state);

		addVehcileDynamicConnections(vehicleObjectList,
			innerDynamicObjects, graph, state);

		// visibleObjects.addAll(innerStaticObjects);
		visibleObjects.addAll(innerDynamicObjects);
		
		this.visibleObjects.set(visibleObjects);
		this.graph.set(graph);
	    }
	} catch (InterruptedException e) {
	    // TODO decide what to do now, start the job again?
	    e.printStackTrace();
	} catch (RuntimeException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public Graph<IWorldObjectBase<T>> getGraph() {
	return graph.get();
    }

    public List<IWorldDynamicObject<T>> getDynamicObjectList() {
	return dynamicObjectList;
    }

    public List<IWorldObjectBase<T>> getVisibleObjectList() {
	return visibleObjects.get();
    }

    public void offer(double[] state) {
	inputQueue.offer(state.clone());
    }

    private double getDistance(Circle circleA, Circle circleB) {
	return circleA.A.distance(circleB.A) - circleA.radius - circleB.radius;
    }

    private LinkedList<IWorldDynamicObject<T>> addDynamicDynamicConnections(
	    LinkedList<IWorldDynamicObject<T>> innerDynamicObjects,
	    LinkedList<IWorldDynamicObject<T>> outerDynamicObjects,
	    Graph<IWorldObjectBase<T>> graph, double[] state) {

	innerDynamicObjects = new LinkedList<>(innerDynamicObjects);
	LinkedList<IWorldDynamicObject<T>> outList = new LinkedList<IWorldDynamicObject<T>>();

	while (!innerDynamicObjects.isEmpty()) {
	    IWorldDynamicObject<T> objectA = innerDynamicObjects.pop();
	    outList.add(objectA);
	    graph.addVertex(objectA);
	    for (IWorldDynamicObject<T> objectB : innerDynamicObjects) {
		double distance = getDistance(objectA.getBoundingCircle(state),
			objectB.getBoundingCircle(state));
		if (distance < MARGIN)
		    graph.addDirectedConnection(objectA, objectB);
	    }
	    ListIterator<IWorldDynamicObject<T>> iterator = outerDynamicObjects
		    .listIterator();
	    while (iterator.hasNext()) {
		IWorldDynamicObject<T> objectB = iterator.next();
		double distance = getDistance(objectA.getBoundingCircle(state),
			objectB.getBoundingCircle(state));
		if (distance < MARGIN) {
		    graph.addDirectedConnection(objectA, objectB);
		    iterator.remove();
		    innerDynamicObjects.add(objectB);
		}
	    }
	}
	return outList;
    }

    private void populateDynamicObjectsLists(double xPosition, double[] state,
	    LinkedList<IWorldDynamicObject<T>> innerDynamicObjects,
	    LinkedList<IWorldDynamicObject<T>> outerDynamicObjects) {
	for (IWorldDynamicObject<T> dynamicObject : dynamicObjectList) {
	    double distance = dynamicObject.getDistanceX(xPosition, state);
	     //if (dynamicObject.getType() == DynamicTypes.Bridge)
	     //continue;// A bridge can´t collide with a road etc
	    if (distance <= INNER_DISTANCE_MARGIN) {
		innerDynamicObjects.add(dynamicObject);
	    } else if (distance <= OUTER_DISTANCE_MARGIN)
		outerDynamicObjects.add(dynamicObject);
	}
    }

    private void populateStaticObjectsLists(double xPosition,
	    LinkedList<IWorldStaticObject<T>> innerStaticObjects,
	    LinkedList<IWorldStaticObject<T>> outerStaticObjects) {
	for (IWorldStaticObject<T> staticObject : staticObjectList) {
	    double distance = staticObject.getShortestDistanceX(xPosition);
	    if (distance <= INNER_DISTANCE_MARGIN) {
		innerStaticObjects.add(staticObject);
	    } else if (distance <= OUTER_DISTANCE_MARGIN)
		outerStaticObjects.add(staticObject);
	}
    }

    private void addDynamicStaticConnections(
	    LinkedList<IWorldDynamicObject<T>> activeDynamicObjects,
	    LinkedList<IWorldStaticObject<T>> innerStaticObjects,
	    LinkedList<IWorldStaticObject<T>> outerStaticObjects,
	    Graph<IWorldObjectBase<T>> graph, double[] state) {
	for (IWorldDynamicObject<T> dynamicObject : activeDynamicObjects) {
	    switch (dynamicObject.getType()) {
	    case Bridge:
	    case Pendulum:
	    case DoublePendulum:
		continue;
	    default:
		Circle circle = dynamicObject.getBoundingCircle(state);
		for (IWorldStaticObject<T> staticObject : innerStaticObjects) {
		    double distance = staticObject
			    .getShortestDistanceX(circle.A.X) - circle.radius;
		    if (distance < MARGIN)
			graph.addDirectedConnection(dynamicObject,
				staticObject);
		}
		for (IWorldStaticObject<T> staticObject : outerStaticObjects) {
		    double distance = staticObject
			    .getShortestDistanceX(circle.A.X) - circle.radius;
		    if (distance < MARGIN)
			graph.addDirectedConnection(dynamicObject,
				staticObject);
		}
	    }
	}
    }

    private void addVehicleStaticConnections(
	    List<IVehicleObject<T>> vehicleObjects,
	    LinkedList<IWorldStaticObject<T>> innerStaticObjects,
	    Graph<IWorldObjectBase<T>> graph, double[] state) {
	for (IVehicleObject<T> vehicleObject : vehicleObjects) {
	    Circle circle = vehicleObject.getBoundingCircle(state);
	    for (IWorldStaticObject<T> staticObject : innerStaticObjects) {
		double distance = staticObject.getShortestDistanceX(circle.A.X)
			- circle.radius;
		if (distance < MARGIN)
		    graph.addDirectedConnection(vehicleObject, staticObject);
	    }
	}
    }

    private void addVehcileDynamicConnections(
	    List<IVehicleObject<T>> vehicleObjects,
	    LinkedList<IWorldDynamicObject<T>> innerDynamicObjects,
	    Graph<IWorldObjectBase<T>> graph, double[] state) {
	for (IVehicleObject<T> vehicleObject : vehicleObjectList) {
	    Circle circle = vehicleObject.getBoundingCircle(state);
	    for (IWorldDynamicObject<T> dynamicObject : innerDynamicObjects) {
		double distance = getDistance(circle,
			dynamicObject.getBoundingCircle(state));
		if (distance < MARGIN) {
		    graph.addDirectedConnection(vehicleObject, dynamicObject);
		}
	    }

	}
    }

}
