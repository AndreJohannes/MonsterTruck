package com.studiocinqo.monstertruck;

import com.studiocinqo.monstertruck.GameEngine.GameEngineFactory;
import com.studiocinqo.monstertruck.OpenGL.GLRenderer;

import GameEngine.Solver.SolutionContainer;
import Graphics.DebugBoard;
import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class GameGLSurfaceView extends GLSurfaceView implements Runnable {

    private final GLRenderer renderer;
    private final GameEngineFactory gameEngineFactory;
    private Thread thread;
    private boolean running = false;

    public GameGLSurfaceView(Context context, AttributeSet attrs) {
	super(context, attrs);
	setEGLContextClientVersion(2);
	
	renderer = new GLRenderer(context);
	setRenderer(renderer);

	gameEngineFactory = GameEngineFactory.getInstance(context, renderer);
	
	// Render the view only when there is a change in the drawing data
	//setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

	thread = new Thread(this);
	getHolder().addCallback(this);
    }

    @Override
    public void onPause() {
	super.onPause();
	renderer.onPause();
    }

    @Override
    public void onResume() {
	super.onResume();
	renderer.onResume();
    }

    @Override
    public void run() {
	double[] state = gameEngineFactory.getInitialState();
	long previousTime = System.currentTimeMillis();
	while (running) {
	    gameEngineFactory.updateScenery(state);
	    SolutionContainer solution = gameEngineFactory.solve(0.12 / 4,
		    state);
	    solution = gameEngineFactory.solve(0.12 / 4, solution.state);
	    solution = gameEngineFactory.solve(0.12 / 4, solution.state);
	    solution = gameEngineFactory.solve(0.12 / 4, solution.state);
	    gameEngineFactory.draw(renderer, solution);
	    state = solution.state;
	    try {
		long timeout = 1000 / 60
			- (System.currentTimeMillis() - previousTime);
		Thread.sleep(timeout > 0 ? timeout : 0);
		previousTime = System.currentTimeMillis();
		if (timeout < 0)
		    DebugBoard.getInstance().incMissedFrames();
	    } catch (InterruptedException e) {
		throw new RuntimeException(e);
	    }
	}
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
	super.surfaceCreated(holder);
	running = true;
	if (thread.getState() == Thread.State.TERMINATED)
	    thread = new Thread(this);
	thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
	super.surfaceDestroyed(holder);
	boolean retry = true;
	running = false;
	while (retry) {
	    try {
		thread.join();
		retry = false;
	    } catch (InterruptedException e) {
	    }
	}
    }

}