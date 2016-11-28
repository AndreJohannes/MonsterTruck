package Auxiliary;

public interface ICallbackFunction {

    public static class CallbackContainer {

	public final double J;
	public final double distance;
	public final Vector2D normal;
	public final ICallbackFunction callbackFunction;

	public CallbackContainer(double distance, Vector2D normal, double J,
		CollisionObject object) {
	    this.distance = distance;
	    this.normal = normal;
	    this.J = J;
	    this.callbackFunction = object.callbackFunction;
	}

    }

    public void callbackFunctionImpulse(double distance, Vector2D normal,
	    double J);

    public void callbackFunctionForce(Vector2D force);

    public double callbackImpulseModifier(Vector2D normal);

}
