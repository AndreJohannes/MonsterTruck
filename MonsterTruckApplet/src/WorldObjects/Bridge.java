package WorldObjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import Auxiliary.Vector2D;
import Graphics.GameColors;

public class Bridge extends BridgeBase<Graphics> {

	public Bridge(Vector2D startPoint, Vector2D endPoint, int numberOfLinks,
			WorldObjects.BridgeBase.Suspension suspension) {
		super(startPoint, endPoint, numberOfLinks, suspension);
	}

	@Override
	public void draw(Graphics graphics, double[] state, double offsetX) {
	Vector2D previousPoint = this.startPoint;
		Vector2D point = null;
		graphics.setColor(GameColors.WINE);
		Stroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_MITER);
		Stroke stroke2 = new BasicStroke(3, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_MITER);
		Stroke stroke1 = new BasicStroke(7, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_MITER);

		graphics.setColor(GameColors.WINE);
		((Graphics2D) graphics).setStroke(stroke);
		int offset = this.offset;
		for (int i = 0; i < (numberOfLinks - 1); i++) {
		    point = new Vector2D(state[offset], state[offset + 1]);
		    graphics.setColor(GameColors.GREY);
		    graphics.drawLine((int) (previousPoint.X - offsetX),
			    (int) (previousPoint.Y
				    - suspension.function(i / (double) numberOfLinks)),
			    (int) (point.X - offsetX), (int) (point.Y - suspension
				    .function((i + 1) / (double) numberOfLinks)));
		    graphics.setColor(GameColors.GREY_DARKER);
		    graphics.drawLine((int) (previousPoint.X - offsetX),
			    (int) (previousPoint.Y+1
				    - suspension.function(i / (double) numberOfLinks)),
			    (int) (point.X - offsetX), 1+(int) (point.Y - suspension
				    .function((i + 1) / (double) numberOfLinks)));
		    graphics.setColor(GameColors.GREY);
		    graphics.drawLine((int) (previousPoint.X - offsetX),
			    (int) (previousPoint.Y+2
				    - suspension.function(i / (double) numberOfLinks)),
			    (int) (point.X - offsetX),2+ (int) (point.Y - suspension
				    .function((i + 1) / (double) numberOfLinks)));
		    
		    graphics.setColor(GameColors.GREY);
		    graphics.drawLine((int) (point.X - offsetX),
			    (int) (point.Y - suspension
				    .function((i + 1) / (double) numberOfLinks)),
			    (int) (point.X - offsetX), (int) point.Y);
		    graphics.setColor(GameColors.GREY_DARKER);
		    graphics.drawLine((int) (-1+point.X - offsetX),
			    (int) (point.Y - suspension
				    .function((i + 1) / (double) numberOfLinks)),
			    (int) (-1+point.X - offsetX), (int) point.Y);
		    graphics.setColor(GameColors.GREY);
		    graphics.drawLine((int) (1+point.X - offsetX),
			    (int) (point.Y - suspension
				    .function((i + 1) / (double) numberOfLinks)),
			    (int) (1+point.X - offsetX), (int) point.Y);
		    previousPoint = point;
		    offset += 4;
		}
		 graphics.setColor(GameColors.GREY);
		graphics.drawLine((int) (previousPoint.X - offsetX),
			(int) (previousPoint.Y
				- suspension.function(1d - 1d / numberOfLinks)),
			(int) (endPoint.X - offsetX),
			(int) (endPoint.Y - suspension.function(1d)));
		 graphics.setColor(GameColors.GREY_DARKER);
		graphics.drawLine((int) (previousPoint.X - offsetX),
			(int) (previousPoint.Y+1
				- suspension.function(1d - 1d / numberOfLinks)),
			(int) (endPoint.X - offsetX),
			1+(int) (endPoint.Y - suspension.function(1d)));
		 graphics.setColor(GameColors.GREY);
		graphics.drawLine((int) (previousPoint.X - offsetX),
			(int) (previousPoint.Y+2
				- suspension.function(1d - 1d / numberOfLinks)),
			(int) (endPoint.X - offsetX),
			(int) (2+endPoint.Y - suspension.function(1d)));
		
		((Graphics2D) graphics).setStroke(stroke1);
		graphics.setColor(new Color(0x505050));
		offset = this.offset;
		previousPoint = this.startPoint;
		for (int i = 0; i < (numberOfLinks - 1); i++) {
		    point = new Vector2D(state[offset], state[offset + 1]);
		    graphics.drawLine((int) (previousPoint.X - offsetX),
			    (int) previousPoint.Y, (int) (point.X - offsetX),
			    (int) point.Y);
		    previousPoint = point;
		    offset += 4;
		}
		graphics.drawLine((int) (previousPoint.X - offsetX),
			(int) previousPoint.Y, (int) (endPoint.X - offsetX),
			(int) endPoint.Y);
	}


}
