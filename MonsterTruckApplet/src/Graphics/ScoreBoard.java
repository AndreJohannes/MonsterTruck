package Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ScoreBoard implements IScoreBoard<Graphics> {

    private static int positionScoreX = 25;
    private static int positionScoreY = 33;
    private static int positionHealthX = 25;
    private static int positionHealthY = 50;
    private static int heightHealth = 10;
    private static int widthHealth = 100;

    private final Font font = new Font("TimesRoman", Font.PLAIN, 18);

	@Override
	public void draw(Graphics gr, int score, double health) {
		gr.setFont(font);
		gr.drawString("SCORE: " + Integer.toString(score), positionScoreX,
			positionScoreY);
		gr.setColor(Color.WHITE);
		gr.fillRect(positionHealthX, positionHealthY, widthHealth,
			heightHealth);
		gr.setColor(GameColors.WINE);
		gr.fillRect(positionHealthX, positionHealthY,
			(int) (health * widthHealth), heightHealth);
		
	}

}
