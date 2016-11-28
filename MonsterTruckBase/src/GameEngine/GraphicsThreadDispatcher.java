package GameEngine;

import java.util.concurrent.SynchronousQueue;

import GameEngine.Solver.SolutionContainer;

public class GraphicsThreadDispatcher<T> implements Runnable {

    private static class GraphicsContainer<T> {

	public final T graphics;
	public final double[] state;
	public final double[] vehicleData;

	public GraphicsContainer(T graphics, SolutionContainer solution) {
	    this.graphics = graphics;
	    this.state = solution.state.clone(); // So we have keep the current
						 // state
	    this.vehicleData = solution.data;
	}

    }

    private final GameEngineFactoryBase<T> gameEngine;
    private final SynchronousQueue<GraphicsContainer<T>> inputQueue;

    public GraphicsThreadDispatcher(GameEngineFactoryBase<T> gameEngine) {
	this.gameEngine = gameEngine;
	this.inputQueue = new SynchronousQueue<GraphicsContainer<T>>();
	(new Thread(this, "GrphsDpt")).start();
    }

    @Override
    public void run() {
	GraphicsContainer<T> graphicsContainer = null;
	try {
	    while ((graphicsContainer = inputQueue.take()) != null) {
		gameEngine._draw(graphicsContainer.graphics,
			graphicsContainer.state, graphicsContainer.vehicleData);
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

    }

    public void offer(T graphics, SolutionContainer solution) {
	inputQueue.offer(new GraphicsContainer<T>(graphics, solution));
    }

}
