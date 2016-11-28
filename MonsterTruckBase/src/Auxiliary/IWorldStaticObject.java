package Auxiliary;

import Auxiliary.EWorldObjects.StaticTypes;

public interface IWorldStaticObject<T> extends IWorldObjectBase<T> {

	public void draw(T graphics, double offset);

	public double getShortestDistanceX(double xPosition);

	public CollisionMatrix getPointCollision(CollisionObject object);

	public CollisionObjectBase getCollisionObject();
	
	public StaticTypes getType();
}
