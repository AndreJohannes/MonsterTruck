package GameEngine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.SynchronousQueue;

import Auxiliary.ArrayHelpers;
import Auxiliary.CollisionMatrix;
import Auxiliary.CollisionObject;
import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.DynamicTypes;
import Auxiliary.EWorldObjects.TokenTypes;
import Auxiliary.Graph;
import Auxiliary.IVehicleObject;
import Auxiliary.ICallbackFunction.CallbackContainer;
import Auxiliary.IWorldDynamicObject;
import Auxiliary.IWorldNoninteractingObject;
import Auxiliary.IWorldObjectBase;
import Auxiliary.IWorldStaticObject;
import Auxiliary.IWorldTokenObject;
import Auxiliary.Vector2D;
import CollisionHandling.CollisionHandler;
import CollisionHandling.PreCollisionHandler;
import GameEngine.Solver.Function;
import GameEngine.Solver.SolutionContainer;
import Graphics.DebugBoard;
import Graphics.IBackground;
import Graphics.IScoreBoard;
import VehicleObjects.VehicleBase;
import VehicleObjects.Wheel;
import WorldObjects.BlockBase;
import WorldObjects.CoinBase;
import XMLParser.XMLVehicleFileReaderBase;
import XMLParser.XMLWorldFileReaderBase;

public abstract class GameEngineFactoryBase<T> implements Function {

    private class Container {

        private double[] derivatives;
        private List<IWorldDynamicObject<T>> dynamicObjects;
        private List<CollisionMatrix> frontWheelObjects;
        private List<CollisionMatrix> rearWheelObjects;

        private Container(double[] derivatives,
                          List<IWorldDynamicObject<T>> dynamicObjcts,
                          List<CollisionMatrix> frontWheelObjects,
                          List<CollisionMatrix> rearWheelObjects) {
            this.derivatives = derivatives;
            this.dynamicObjects = dynamicObjcts;
            this.frontWheelObjects = frontWheelObjects;
            this.rearWheelObjects = rearWheelObjects;
        }
    }

    private Container container;

    private final VehicleBase<T> vehicle;
    private final Solver solver;
    private final IBackground<T> background;
    private final IBackground<T> landscape;
    private final PreCollisionHandler<T> preCollisionHandler;
    private final CollisionHandler<T> collisionHandler;
    private final ScoreAndHealth<T> scoreAndHealth;
    private final GraphicsThreadDispatcher<T> graphicsDispatcher;
    private List<IWorldNoninteractingObject<T>> testList;

    protected GameEngineFactoryBase(XMLVehicleFileReaderBase<T> readerVehcile,
                                    XMLWorldFileReaderBase<T> readerWorld, IBackground<T> background,
                                    IBackground<T> landscape, IScoreBoard<T> scoreBoard) {
        this.vehicle = readerVehcile.getVehicle();
        solver = new Solver.RungeKuttaSolver(this);
        this.background = background;
        this.landscape = landscape;
        this.scoreAndHealth = new ScoreAndHealth<T>(scoreBoard);
        collisionHandler = new CollisionHandler<T>();
        testList = readerWorld.getNoninteractingObjectList();
        List<IWorldStaticObject<T>> staticObjects = readerWorld
                .getStaticObjectList();
        List<IWorldDynamicObject<T>> dynamicObjects = readerWorld
                .getDynamicObjectList();
        List<IWorldTokenObject<T>> tokenObjects = readerWorld
                .getTokenObjectList();
        this.preCollisionHandler = new PreCollisionHandler<T>(staticObjects,
                dynamicObjects, tokenObjects, vehicle.getVehicleParts());
        this.graphicsDispatcher = new GraphicsThreadDispatcher<T>(this);
    }

    public double[] concat(double[] a, double[] b) {
        int aLen = a.length;
        int bLen = b.length;
        double[] c = new double[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public final void draw(T graphics, SolutionContainer solution) {
        graphicsDispatcher.offer(graphics, solution);
    }

    @Override
    public SolutionContainer function(double[] state) {

        Container container = this.container;
        double[] outstate = container.derivatives.clone();

        for (IWorldDynamicObject<T> object : container.dynamicObjects)
            object.evaluateForce(state, outstate);

        double[] data = vehicle.function(state, outstate,
                container.frontWheelObjects, container.rearWheelObjects);
        return new SolutionContainer(outstate, data);

    }

    public double[] getInitialState() {
        double[] state = new double[]{400, 100, 0, 0, -0.3, 0, 0, 0};
        for (IWorldDynamicObject<T> object : preCollisionHandler
                .getDynamicObjectList()) {
            int offset = state.length;
            object.setOffset(offset);
            state = concat(state, object.initialState());
        }
        return state;
    }

    public VehicleBase<T> getVehicle() {
        return vehicle;
    }

    public SolutionContainer solve(double stepSize, double[] state) {
        container = handleImpact(state);
        return solver.solveStep(state, stepSize);
    }

    public void updateScenery(double[] state) {
        preCollisionHandler.offer(state);
        double vehicleHealth = vehicle.getHealth();
        if (vehicleHealth <= 0) {
            vehicle.setHealth(100.d);
            ArrayHelpers.ArrayCopyDoubles(getInitialState(), state);
        }
        for (IWorldTokenObject<T> object : vehicle.collectTokens(state)) {
            if (object.getType() == TokenTypes.Coin && object.isActive()) {
                object.setActive(false);
                scoreAndHealth.increaseScore(((CoinBase<T>) object).getValue());

            }
        }
        scoreAndHealth.setHealth((int) (vehicle.getHealth()));
    }

    private List<CollisionMatrix> getCollisionList(double[] state,
                                                   double[] derivatives, CollisionObject object,
                                                   Iterable<IWorldObjectBase<T>> iterable,
                                                   List<CollisionMatrix> retList) {

        for (IWorldObjectBase<T> objectB : iterable) {
            switch (objectB.getCategory()) {
                case Dynamic:
                    ((IWorldDynamicObject<T>) objectB).getPointCollision(state,
                            derivatives, object, retList);
                    break;
                case Static:
                    CollisionMatrix collisionMatrix = ((IWorldStaticObject<T>) objectB)
                            .getPointCollision(object);
                    if (collisionMatrix != null)
                        retList.add(collisionMatrix);
                    break;
            }
        }

        return retList;
    }

    private Container handleImpact(double[] state) {
        double[] outstate = new double[state.length];

        Graph<IWorldObjectBase<T>> graph = preCollisionHandler.getGraph();
        List<CallbackContainer> callbackFunctions = Collections
                .synchronizedList(new LinkedList<CallbackContainer>());

        List<CollisionMatrix> frontWheelObjects = new LinkedList<CollisionMatrix>();
        List<CollisionMatrix> rearWheelObjects = new LinkedList<CollisionMatrix>();

        List<IWorldDynamicObject<T>> dynamicObjects = new LinkedList<>();

        Container container = new Container(outstate, dynamicObjects,
                frontWheelObjects, rearWheelObjects);

        // Part of the collision handling is done in parallel
        // the collision handler does the thread handling
        collisionHandler.register(state, outstate, graph, callbackFunctions);

        for (IWorldObjectBase<T> objectA : graph.getVertexIterable()) {
            switch (objectA.getCategory()) {
                case Dynamic:
                    dynamicObjects.add((IWorldDynamicObject<T>) objectA);
                    ((IWorldDynamicObject<T>) objectA).addExternalForce(state,
                            outstate);
                    break;
                case Vehicle:
                    switch (((IVehicleObject<T>) objectA).getType()) {
                        case FrontWheel:
                            getCollisionList(state, outstate,
                                    ((Wheel<T>) objectA).getCollisionObject(state,
                                            outstate, null),
                                    graph.getAdjacentVertexIterable(objectA),
                                    frontWheelObjects);
                            continue;
                        case RearWheel:
                            getCollisionList(state, outstate,
                                    ((Wheel<T>) objectA).getCollisionObject(state,
                                            outstate, null),
                                    graph.getAdjacentVertexIterable(objectA),
                                    rearWheelObjects);
                            continue;
                    }
                    break;
            }

            // Hand off to handle the collision of objectA with all its
            // collision partners
            collisionHandler.offer(objectA);
        }

        collisionHandler.arriveAndAwait();

        // DebugBoard.getInstance().setPreCollisionCount(i);


        for (IWorldDynamicObject<T> object : dynamicObjects) {
            if (object.getType() == DynamicTypes.Block)
                ((BlockBase) object).getCollisionObject(state, outstate).applyAndReset(state, outstate);
        }

        for (CallbackContainer callback : callbackFunctions) {
            callback.callbackFunction.callbackFunctionImpulse(callback.distance,
                    callback.normal, callback.J);
        }

        return container;
    }

    protected void _draw(T graphics, double[] state, double[] data) {

        double offset = (state[0] < 400 ? 400
                : (state[0] > 7500 ? 7500 : state[0])) - 400;
        background.draw(graphics, 0);// For now: static background
        landscape.draw(graphics, offset);
        for (IWorldObjectBase<T> object : preCollisionHandler
                .getVisibleObjectList()) {
            object.draw(graphics, state, offset);
        }

        vehicle.draw(graphics,
                (state[0] < 400 ? state[0]
                        : (state[0] > 7500 ? state[0] - 7500 : 400)),
                state[1], -Math.toDegrees(state[4]), -state[6], -state[7],
                data[0], data[1], data[2], data[3]);
        for (IWorldNoninteractingObject<T> object : testList) {
            object.draw(graphics, offset);
        }
        scoreAndHealth.draw(graphics);
        DebugBoard.getInstance().draw(graphics);
    }

}