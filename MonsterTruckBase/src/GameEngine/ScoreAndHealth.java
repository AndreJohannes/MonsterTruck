package GameEngine;

import Graphics.IScoreBoard;

public class ScoreAndHealth<T> {

    private int score = 0;
    private int health = 100;
    private final IScoreBoard<T> scoreBoard;
    
    public ScoreAndHealth(IScoreBoard<T> scoreBoard){
    	this.scoreBoard = scoreBoard;
    }

    public void increaseScore(int inc) {
	score += inc;
    }

    public void setHealth(int health) {
	this.health = health;
    }

    public void draw(T gr) {
	scoreBoard.draw(gr, score, health / 100d);
    }

}
