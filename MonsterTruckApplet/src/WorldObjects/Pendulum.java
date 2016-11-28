package WorldObjects;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import Auxiliary.Vector2D;
import Graphics.GameColors;

public class Pendulum extends PendulumBase<Graphics> {

    private Stroke stroke = new BasicStroke(3, BasicStroke.CAP_ROUND,
	    BasicStroke.JOIN_MITER);

    public Pendulum(Vector2D center, double radius) {
	super(center, radius);
    }

    @Override
    public void draw(Graphics graphics, double[] state, double offsetX) {
	double positionR = state[offset];
	graphics.setColor(GameColors.WINE);
	((Graphics2D) graphics).setStroke(stroke);
	graphics.drawLine((int) (center.X - offsetX), (int) center.Y,
		(int) (center.X - offsetX + radius * Math.cos(positionR)),
		(int) (center.Y + radius * Math.sin(positionR)));
	graphics.fillOval(
		(int) (center.X - offsetX + radius * Math.cos(positionR) - 20),
		(int) (center.Y + radius * Math.sin(positionR) - 20), 40, 40);
    }

}
