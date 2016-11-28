import GameEngine.GameEngineFactory;
import Graphics.DebugBoard;
import OpenGL.GLRenderer;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class StartApplet extends Applet implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;
    private GameEngineFactory engine;
    private BufferStrategy bufferStrategy;
    private Canvas renderer;

    public void init() {
        setSize(1280, 720);
        setFocusable(true);
        this.setLayout(new BorderLayout());
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(glp);
        capabilities.setDoubleBuffered(true);
        renderer = new GLRenderer();
        setIgnoreRepaint(true);

        Frame frame = (Frame) this.getParent().getParent();
        frame.setMenuBar(null);
        frame.setTitle("MonsterTruck Concept");

        addKeyListener(this);

    }

    @Override
    public void start() {
        setSize(1280, 720);
        renderer.setPreferredSize(new Dimension(getWidth(), getHeight()));
        add(renderer, BorderLayout.CENTER);
        engine = new GameEngineFactory(this, (GLRenderer) renderer);
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        double[] state = engine.getInitialState();
        long previousTime = System.currentTimeMillis();
        while (true) {
            engine.updateScenery(state);
            GameEngine.Solver.SolutionContainer solution = engine
                    .solve(0.12 / 4, state);
            solution = engine.solve(0.12 / 4, solution.state);
            solution = engine.solve(0.12 / 4, solution.state);
            solution = engine.solve(0.12 / 4, solution.state);
            engine.draw((GLRenderer) renderer, solution);
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

}
