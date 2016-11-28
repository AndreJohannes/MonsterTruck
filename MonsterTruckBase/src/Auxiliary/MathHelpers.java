package Auxiliary;

public class MathHelpers {
    public static double mod(double x, double y) {
	double result = x % y;
	return result < 0 ? result + y : result;
    }

}
