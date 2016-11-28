package Auxiliary;


import Auxiliary.EWorldObjects.NoninteractTypes;

public interface IWorldNoninteractingObject<T> {

    public void draw(T graphics, double offset);

    public double getShortestDistanceX(double xPosition);

    public NoninteractTypes getType();

}
