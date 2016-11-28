package Auxiliary;

import java.util.List;

import Auxiliary.EWorldObjects.VehiclePartTypes;
import Auxiliary.Vector2D.Circle;

public interface IVehicleObject<T> extends IWorldObjectBase<T> {


	public Circle getBoundingCircle(double[] state);

	public CollisionObject getCollisionObject(double[] state, double[] derivatives, Vector2D point);

	public double getDistanceX(double xPosition, double[] state);

	public void getPointCollision(double[] state, double[] derivtives, CollisionObject pointSource,
			List<CollisionMatrix> outList);

	public VehiclePartTypes getType();

}
