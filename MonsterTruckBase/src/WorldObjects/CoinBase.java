package WorldObjects;

import Auxiliary.EWorldObjects.Category;
import Auxiliary.EWorldObjects.TokenTypes;

import java.awt.Graphics;

import Auxiliary.IWorldTokenObject;
import Auxiliary.Vector2D;

public abstract class CoinBase<T> implements IWorldTokenObject<T> {

    protected final Vector2D center;
    protected boolean active = true;
    private int value = 100;

    public CoinBase(Vector2D center) {
	this.center = center;
    }

    @Override
    public abstract void draw(T graphics, double offset);

    @Override
    public void draw(T graphics, double[] state, double offset) {
	draw(graphics, offset);
    }

    @Override
    public double getDistance(Vector2D location) {
	return center.distance(location);
    }

    @Override
    public double getDistanceX(double xPosition) {
	return Math.abs(center.X - xPosition);
    }

    @Override
    public TokenTypes getType() {
	return TokenTypes.Coin;
    }

    public int getValue() {
	return value;
    }

    @Override
    public boolean isActive() {
	return active;
    }

    @Override
    public void setActive(boolean active) {
	this.active = active;
    }

    @Override
    public Category getCategory() {
	return Category.Token;
    }

}
