package Graphics;

import java.awt.Font;
import java.awt.Graphics;

public class DebugBoard {

    private static DebugBoard _instance = null;

    //private final Font font = new Font("TimesRoman", Font.PLAIN, 18);
    private int precollisionCount = 0;
    private int missedFrames = 0;

    public static DebugBoard getInstance() {
	if (_instance == null) {
	    _instance = new DebugBoard();
	}
	return _instance;
    }

    private DebugBoard() {

    }

    public void setPreCollisionCount(int count) {
	precollisionCount = count;
    }

    public void incMissedFrames() {
	missedFrames++;
    }

    public <T> void draw(T gr) {
//	 if(gr instanceof Graphics){
//	 draw((Graphics)gr);
//	 }
    }

//     public void draw(Graphics gr) {
//     //gr.setFont(font);
//     gr.drawString(
//     "Precollision count: " + Integer.toString(precollisionCount),
//     1000, 20);
//    gr.drawString("Missed frames: " + Integer.toString(missedFrames), 1000,
//     40);
//
//     }

}
