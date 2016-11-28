import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import GameEngine.GameEngineFactory;
import Graphics.DebugBoard;
import Graphics.TextureAtlas;

public class StartApplet extends Applet implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;
    private GameEngineFactory engine;
    private BufferStrategy bufferStrategy;
    private Canvas drawArea;

    public void init() {
	setSize(1280, 720);
	setFocusable(true);
	drawArea = new Canvas();
	setIgnoreRepaint(true);
	addKeyListener(this);
	Frame frame = (Frame) this.getParent().getParent();
	frame.setMenuBar(null);
	frame.setTitle("MonsterTruck Concept");
    }

    @Override
    public void start() {
	setSize(1280, 720);
	drawArea.setSize(new Dimension(getWidth(), getHeight()));
	add(drawArea);
	createBufferStrategy(2);
	engine = new GameEngineFactory(this, drawArea.getBufferStrategy(),
		new TextureAtlas());
	Thread thread = new Thread(this);
	thread.start();
    }

    public void run() {
	double[] state = engine.getInitialState();
	bufferStrategy = drawArea.getBufferStrategy();
	long previousTime = System.currentTimeMillis();
	while (true) {
	    engine.updateScenery(state);
	    GameEngine.Solver.SolutionContainer solution = engine
		    .solve(0.12 / 4, state);
	    solution = engine.solve(0.12 / 4, solution.state);
	    solution = engine.solve(0.12 / 4, solution.state);
	    solution = engine.solve(0.12 / 4, solution.state);
	    Graphics g = bufferStrategy.getDrawGraphics();
	    if (g == null)
		continue;
	    engine.draw(g, solution);
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
    public void paint(Graphics g) {
    }

    @Override
    public void update(Graphics g) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
	switch (e.getKeyCode()) {
	case KeyEvent.VK_RIGHT:
	    engine.getVehicle().setThrottle(12);
	    break;
	case KeyEvent.VK_LEFT:
	    engine.getVehicle().setThrottle(-5);
	    break;
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	switch (e.getKeyCode()) {
	case KeyEvent.VK_RIGHT:
	case KeyEvent.VK_LEFT:
	    engine.getVehicle().setThrottle(0);
	}
    }

    public void createBufferStrategy(int numBuffers) {
	drawArea.createBufferStrategy(numBuffers);
    }
}
