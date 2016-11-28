package Auxiliary;

import java.util.List;

import Auxiliary.EWorldObjects.DynamicTypes;
import Auxiliary.Vector2D.Circle;

public interface IWorldDynamicObject<T> extends IWorldObjectBase<T> {

    public void addExternalForce(double[] state, double[] derivatives);

    public void draw(T graphics, double[] state, double offset);

    public void evaluateForce(double[] state, double[] derivatives);

    public Circle getBoundingCircle(double[] state);
    
    @Deprecated
    public CollisionObject getCollisionObject(double[] state,
	    double[] derivatives, Vector2D point);
    
    public CollisionDynamicObjectBase getCollisionObject(double[] state,
	    double[] derivatives);
    
    public double getDistanceX(double xPosition, double[] state);

    public int getOffset();

    @Deprecated
    public void getPointCollision(double[] state, double[] derivtives,
	    CollisionObject pointSource, List<CollisionMatrix> outList);
    
    public DynamicTypes getType();

    public double[] initialState();

    public void setOffset(int offset);

}
