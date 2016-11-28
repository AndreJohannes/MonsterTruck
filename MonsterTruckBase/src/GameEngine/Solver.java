package GameEngine;

public abstract class Solver {

    public static class RungeKuttaSolver extends Solver{

	public RungeKuttaSolver(Function function) {
	    super(function);
	}
	
	 public SolutionContainer solveStep(double[] state, double stepSize) {
		SolutionContainer k1 = function.function(state);
		SolutionContainer k2 = function.function(doubleArrayAdd(state, doubleArrayScale(k1.state, stepSize / 2.0)));
		SolutionContainer k3 = function.function(doubleArrayAdd(state, doubleArrayScale(k2.state, stepSize / 2.0)));
		SolutionContainer k4 = function.function(doubleArrayAdd(state, doubleArrayScale(k3.state, stepSize)));
	        
	        double[] resultState = doubleArrayScale(doubleArrayAdd(doubleArrayScale(doubleArrayAdd(k2.state, k3.state), 2.0), doubleArrayAdd(k1.state, k4.state)), stepSize / 6.0);
	        double[] resultData = doubleArrayScale(doubleArrayAdd(doubleArrayScale(doubleArrayAdd(k2.data, k3.data), 2.0), doubleArrayAdd(k1.data, k4.data)), 1. / 6.0);
	        return new SolutionContainer(doubleArrayAdd(state, resultState), resultData);
	    }
	
    }
    
    public static class EulerSolver extends Solver{

	public EulerSolver(Function function) {
	    super(function);
	}
	
	 public SolutionContainer solveStep(double[] state, double stepSize) {
		SolutionContainer k1 = function.function(state);
	        
	        return new SolutionContainer(doubleArrayAdd(state, doubleArrayScale(k1.state,stepSize)), k1.data);
	    }
	
    }
    
    public static class SolutionContainer {

        public final double[] state; // The state
        public final double[] data; // some auxiliary data

        public SolutionContainer(double[] state, double[] data) {
            this.state = state;
            this.data = data;
        }
    }

    public static interface Function {
        public SolutionContainer function(double[] state);
    }

    protected final Function function;

    public Solver(Function function) {
        this.function = function;
    }

    public abstract SolutionContainer solveStep(double[] state, double stepSize);

    protected double[] doubleArrayAdd(double[] a, double[] b) {
        if (a.length != b.length)
            throw new RuntimeException("Arrays do not have same length");

        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    protected double[] doubleArrayScale(double[] a, double scalar) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = scalar * a[i];
        }
        return result;
    }
	
}
