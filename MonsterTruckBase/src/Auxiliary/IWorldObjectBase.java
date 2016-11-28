package Auxiliary;

import Auxiliary.EWorldObjects.Category;

public interface IWorldObjectBase<T> {

    public Category getCategory();

    public void draw(T graphics, double[] state, double offset);

}
