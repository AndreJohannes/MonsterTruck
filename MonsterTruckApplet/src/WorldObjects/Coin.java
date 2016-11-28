package WorldObjects;

import java.awt.Font;
import java.awt.Graphics;

import Auxiliary.Vector2D;
import Graphics.GameColors;

public class Coin extends CoinBase<Graphics> {

	public Coin(Vector2D center) {
		super(center);
	}

	@Override
	public void draw(Graphics graphics, double offset) {
	    if (!active)
		    return;
		graphics.setColor(GameColors.GOLD);
		graphics.fillOval((int) (center.X - offset), (int) (center.Y), 40, 40);
		graphics.setColor(GameColors.SILVER);
		graphics.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		graphics.drawString("$", (int) (center.X - offset + 10),
			(int) center.Y + 30);
	}

	

}
