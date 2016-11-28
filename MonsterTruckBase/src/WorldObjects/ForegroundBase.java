package WorldObjects;

import Auxiliary.EWorldObjects.NoninteractTypes;
import Auxiliary.IWorldNoninteractingObject;
import Auxiliary.Vector2D;

public abstract class ForegroundBase<T> implements IWorldNoninteractingObject<T> {

    protected final Vector2D pointA;
    protected final Vector2D pointB;

    public ForegroundBase(Vector2D pointA, Vector2D pointB) {
	this.pointA = pointA;
	this.pointB = pointB;
    }

    @Override
    public abstract void draw(T graphics, double offset);

    @Override
    public double getShortestDistanceX(double xPosition) {
	return 0;
    }

    @Override
    public NoninteractTypes getType() {
	return NoninteractTypes.Foregound;
    }

}
