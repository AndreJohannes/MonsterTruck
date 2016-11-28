package Auxiliary;


import Auxiliary.EWorldObjects.TokenTypes;

public interface IWorldTokenObject<T> extends IWorldObjectBase<T> {

    public void draw(T graphics, double offset);

    public double getDistanceX(double xPosition);

    public double getDistance(Vector2D location);

    public boolean isActive();

    public void setActive(boolean active);

    public TokenTypes getType();
}
