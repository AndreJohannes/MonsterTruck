package WorldObjects;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import Auxiliary.Vector2D;
import Graphics.GameColors;

public class DoublePendulum extends DoublePendulumBase<Graphics> {

    private Stroke stroke = new BasicStroke(3, BasicStroke.CAP_ROUND,
	    BasicStroke.JOIN_MITER);

    public DoublePendulum(Vector2D center) {
	super(center);
    }

    @Override
    public void draw(Graphics graphics, double[] state, double offsetX) {
	double theta1 = state[offset];
	double theta2 = state[offset + 1];
	graphics.setColor(GameColors.WINE);
	((Graphics2D) graphics).setStroke(stroke);
	Vector2D p1 = new Vector2D(center.X + L1 * Math.sin(theta1),
		center.Y + L1 * Math.cos(theta1));
	Vector2D p2 = new Vector2D(p1.X + L2 * Math.sin(theta2),
		p1.Y + L2 * Math.cos(theta2));
	graphics.drawLine((int) (center.X - offsetX), (int) center.Y,
		(int) (p1.X - offsetX), (int) (p1.Y));
	graphics.fillOval((int) (p1.X - offsetX - 10), (int) (p1.Y - 10), 20,
		20);
	graphics.drawLine((int) (p1.X - offsetX), (int) p1.Y,
		(int) (p2.X - offsetX), (int) (p2.Y));
	graphics.fillOval((int) (p2.X - offsetX - 10), (int) (p2.Y - 10), 20,
		20);
    }

}
